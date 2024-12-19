import com.google.gson.JsonParseException;
import io.vavr.control.Try;
import lombok.val;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.snack.compiler.exception.BugReportException;
import org.snack.compiler.lexer.Lexer;
import org.snack.compiler.lexer.SyntaxElement;
import org.snack.compiler.lexer.Token;
import org.snack.compiler.lexer.TokenScanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import spark.Spark;
import com.google.gson.Gson;

public class Main {


    private static List<Token> lexerService(@NonNull String sourceCode) throws Exception {
        List<Token> tokens = new ArrayList<>();
        try (val tokenScanner = new TokenScanner(sourceCode)) {
            val lexer = new Lexer(tokenScanner);
            Function<Token, Token> id = Function.identity();
            while (!lexer.isAtEOF()) {
                val token = lexer.scanRealNumber().map(id)
                        .or(() -> lexer.scanNewLine().map(id))
                        .or(() -> lexer.scanComment().map(id))
                        .or(() -> lexer.scanIntegerNumber().map(id))
                        .or(() -> lexer.scanStr().map(id))
                        .or(() -> lexer.scanChar().map(id))
                        .or(() -> Arrays.stream(SyntaxElement.values())
                                .map(lexer::scanSyntaxElement)
                                .filter(Optional::isPresent)
                                .findFirst()
                                .orElse(Optional.empty()))
                        .or(() -> lexer.scanIdentifier().map(id))
                        .or(() -> lexer.scanInvalidToken().map(id));
                if (token.isPresent()) {
                    tokens.add(token.get());
                } else {
                    throw new BugReportException("No token was recognized");
                }
            }
            return tokens;
        }
    }

    @Data
    private static class LexerRequest {
        private String sourceCode;
    }

    @Data
    @AllArgsConstructor
    private static class TokenListResponse {
        private List<Token> tokenList;
    }

    @Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String error;
    }

    public static void main(String[] args) {

        Spark.port(8080);
        Spark.post("lexer", (req, res) -> {
            res.type("text/json");

            val gson = new Gson();

            return Try.of(() -> {
                String sourceCode = gson.fromJson(req.body(), LexerRequest.class).getSourceCode();
                res.status(200);
                val responseBody = new TokenListResponse(lexerService(sourceCode));
                return gson.toJson(responseBody, TokenListResponse.class);
            }).recover(JsonParseException.class, exc -> {
                res.status(403);
                val errorResponse = new ErrorResponse(exc.getMessage());
                return gson.toJson(errorResponse);
            }).recover(Exception.class, exc -> {
                res.status(400);
                val errorResponse = new ErrorResponse(exc.getMessage());
                return gson.toJson(errorResponse);
            }).get();
        });

        }

}
