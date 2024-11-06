## Introduction
This file defines snack's features and how it logically works. Implementation details like algorithms complexity and internal structures, synchronization techniques and code optimization are defined by compilers. At the end of this file are described the naming conventions adopted by snack creators.
## Variables
A variable identifies a block of memory with a name. A variable have a type that defines the meaning of the block of memory it refers. The type must be defined at compile time and never change during the program execution as well as the value of the variable. The name of the variable must start with a letter and may contain letters, underscore, numbers, apostrophe or question mark. The following list shows a set of valid identifiers:
```
num
Person
x0
f'
empty?
snake_case
```
The `=` operator allows to assign a value to a variable. The type of the variable may not be specified but is deduced looking at the right operand of `=` operator.
```
x = "snack"
```
In the above line of code the variable "x" is allocated as a string and will be assigned the value `"snack"`.
## Functions
A functions is a block of code that is identified by a name. There are two types of functions, pure functions and unsafe functions.  A Pure function is such that if it guarantees that for a set of arguments the result is always the same (it has no side effect). An unsafe function is a function that may have side effect like reading or writing from a file, accessing to a database or streaming data. Functions have parameters on which depends the result of the function. Parameters and result have a type that is defined at compile time. Type may be of any kind: data types, traits and functions. The syntax of a function is composed by the name of the function, the arguments in parenthesis (for each arguments the type must be specified), the result's type, the `=` operator and the function body. The result of the function corresponds to the result of the expression at the right of `=`.
```
def sum(a int, b int) int = a + b
```
The compiler is able to infer the result's type, in fact it's not mandatory do explicit it. The above function can be written as
```
def sum(a int, b int) = a + b
```
The type of an argument may be omitted, the compiler will consider the type of the successive parameter. Last argument's type must be explicitly defined.
```
def sum(a, b int) = a + b
```
Functions may have default parameter that is to say if a parameter value is not specified its value will be the default value.  There can not be a normal parameter after a default parameter.
```
def add(n, inc int) = n + inc
```
A function with no argument does not need parenthesis but the return type is still mandatory.
```
def half = 1/2
```
A function may have no return type like a function that print a message on the standard output.
```
def sayHelloWorld = println("hello world!!")
```
The function identifier have to start with a letter and may contains letters, numbers, `_`, `?`, `'` and may end with `!`. Functions that end with `!` are unsafe functions, therefore can not be called by a pure function, other details about unsafe functions are given in the [dedicated section](#unsafe-behavior).
### Function call
A function call is like reusing the procedure described by the function. These procedures are parameterized. A function call is made of the name of the function and the list of the parameters enclosed in parentheses. In the following line of code it's declared a variable s that is the difference between 2 and 3.
```
def sub(a, b int) int = a - b
let s = sub(5, 3) #2
```
In the above snippet the arguments are passed by position, it means that the first value refers to the first argument and so on. Snack allows passing values by name making explicit the value of a parameter, just like an assignment.
```
let ss = sub(3, 5) #-2
let s = sub(b=3, a=5) #2
```
It's possible to pass some argument by name and some by position, in that case the relative positional arguments order must be respected.
### Infix function application
For readability purposes `snack` allows infix functions. These functions can be used like operators. The function `def sum(a, b int) = a + b` can be used with the normal syntax (`s = sum(1, 2)`) or with the infix function syntax (`1 sum 2`).  An infix function must not contain more than 2 arguments. Because the infix function can be used as operator is mandatory to establish a priority evaluation. To show this concept let's give an example and create two functions that act as multiplication and addition function.
```
def add(a, b int) = a + b
def mul(a, b int) = a * b
```
Here they are used.
```
v = 1 add 2 mul 3
```
What we would logically expect is that the multiplication is evaluated before the addition. By default infix functions have left priority it means that the above code will result to something like `v = mul(add(1, 2), 3)` and this is not what is expected by a mathematical expression.
To change this behavior snack provides a set of macros that can be called at compile time so change the program behavior. Because the `add` has a priority value lower than `mul` we have to set the level of priority, the higher is the level earlier the expression will be evaluated. This section is not dedicated to metadata so this topic won't be covered, we are just going to say that the metadata to set is `priority_level`.
```
def add(a, b int) = a + b
def mul(a, b int) = a * b

$PRIORITY_LEVEL(add, 7)
$PRIORITY_LEVEL(mul, 8)
```
### Partial function application
When there is the need of calling a functions always with the same parameter `snack` provides a sugar syntax to makes code cleaner. Partial function application consist of creating a copy of the function with some parameters set. An example is a multiplication function `multiply`. This function takes two numbers and return their product. Might happen that a program needs several multiplication by 2, without partial application the implementation would look like
```
def multiply(a, b int) = a * b
def multiplyBy2 = multiply(2)
```
To understand what happens, the compiler will generate the function
```
def multiplyBy2(a int) = multiply(2, a)
```
Partial application can be applied even if the parameters order gets changed.
```
def divide(dividend, divisor double) = a / b
def divideBy2 = divide(divisor=2.0)
def divideBy2' = (divide 2.0)
```
### Expressions
An expression is computation sequence that have a result. There  several type of expressions:
- assignment expression
- algebraic expression
- bit-wise operation
- boolean expression
- literal values
- function call
- do-block
- empty expression
#### Assignment expression
It's the declaration of a variable or a function, it has no result.
#### Algebraic expression
Are expressions that uses algebraic operator: `+`, `-`, `*`, `\`, `%`.
#### Bit-wise operation
Are expressions that uses `<<`, `>>`, `&&`, `||`, `~~`, `^^`
#### Boolean expression
Are expressions that uses  `&`, `|`, `~`
```
def xor(a, b boolean) = ~(a&b) & (a|b)
```
#### Literal values
Literal values are values that can  be evaluated at compile time and have the type of the
#### Function call
A function call is like reusing the procedure described by the function. These procedures are parameterized. A function call is made of the name of the function and the list of the parameters enclosed in parentheses. In the following line of code it's declared a variable s that is the difference between 2 and 3.
```
def sub(a, b int) int = a - b
let s = sub(5, 3) #2
```
In the above snippet the arguments are passed by position, it means that the first value refers to the first argument and so on. Snack allows passing values by name making explicit the value of a parameter, just like an assignment.
```
let ss = sub(3, 5) #-2
let s = sub(b=3, a=5) #2
```
It's possible to pass some argument by name and some by position, in that case the relative positional arguments order must be respected.
##### Infix function
Functions with just 2 parameters can be used as an operator: `sub(2, 3)` is equivalent to `2 sub 3`. Unlike operators there is no priority between infix function, they are evaluated from left to right.
```
def mul(a, b int) = a * b
def sum(a, b int) = a + b
x = 2 sum 3 mul 4 #(2 sum 3) mul 4 = 20
y = 3 mul 4 sum 2 #(3 mul 4) sum 2 = 14
```
#### do-block
A do-block is a set of instruction that will be evaluate to single expression and the final result correspond to the result of the last expression. In fact when a function is composed of multiple line of code they must be enclosed in a do block.
```
def printSum(a, b int) = do
	sum = a + b
	println(sum)
end
```
## Data types
Snack provides some built-in data types that represents, integer numbers, real numbers, boolean values, strings(sequence of characters), collections of instances and custom data type.
### Integer types
Integer types represent subset of rational or natural numbers.
Integer numbers use 2's complement encoding.

| type   | min             | max              | size (byte) |
| ------ | --------------- | ---------------- | ----------- |
| int8   | -2<sup>7</sup>  | 2<sup>7</sup>-1  | 1           |
| int16  | -2<sup>15</sup> | 2<sup>15</sup>-1 | 2           |
| int32  | -2<sup>31</sup> | 2<sup>31</sup>-1 | 4           |
| int64  | -2<sup>63</sup> | 2<sup>63</sup>-1 | 8           |
| word   | *               | *                | *           |
| uint8  | 0               | 2<sup>8</sup>-1  | 1           |
| uint16 | 0               | 2<sup>16</sup>-1 | 2           |
| uint32 | 0               | 2<sup>32</sup>-1 | 4           |
| uint64 | 0               | 2<sup>64</sup>-1 | 8           |
| uword  | 0               | *                | *           |
\* Dependent on platform and compiler

There are aliases names for some of the types in the table above.

| alias name | integer type |
| ---------- | ------------ |
| byte       | uint8        |
| char       | uint16       |
| int        | int32        |
#### Literal integer
Literal integer values are numbers that can be represented in 4 different format: decimal, binary, octal and hexadecimal. The default format is decimal. For specifying other formats the number must be preceded by a 0 and a letter that indicates the format: b for binary, o for octal and h for hexadecimal. The regular expression that match integer values is `0(b|B)[01]+ | 0(o|O)[0..7]+ | [0..9]+ | 0(h|H)[0..9A..Fa..f]+`.
#### Literal characters
A literal character is defined a symbol enclosed in single quotes. Some special characters are encoded as pair of two characters. These are called escape characters, they are all listed in the following table.

| pair | encoded character |
| ---- | ----------------- |
| \n   | new line          |
| \r   | carriage return   |
| \t   | tabulation        |
| \\\\ | back slash        |
A literal character can be identified by it's integer value and the syntax to do this is using the code after a `\`. The example is the character `a` which number value is 65, in fact `'a'` is equivalent to `'\65'`
### Floating point types.
Real number are represented by floating point data that follows the IEE754 standard. Snack defines two floating point number types that represents real values with 32 bit and 64 bit.

| type name | size(byte) |
| --------- | ---------- |
| float32   | 4          |
| float64   | 8          |

Like integers, floating point types define aliases as well.

| alias name | floating type |
| ---------- | ------------- |
| float      | float32       |
| double     | float64       |

### Boolean type
Boolean variables can assume only two values: `true` or `false`.
### String
A string is a sequence of `char`. A literal string is a set of characters between `"`.

### List
A list is a set of sequential elements of the same type. It's identified by `[t]` where t is the type of the items.
```
list = [1, 2, 3, 4, 5, 6, 7]
```
It supports no operation by default but the standard library implements all the necessary methods, to work with list is necessary to use pattern matching.
### Set
A set is a collection of items of the same types with no duplicate. It is identified by `{t}` where `t` is the type of the objects it contains. By default the set uses a compiler generated hash function for storing objects, but this methods has some limits. Snack provides two builtin function for supplying a custom hash function or a compare function.
```
data Person(name, last_name string, age int) impl Hash'
	def hash = name.hash + last_name.hash
end

hashset = newseth(Person.hash)
set = newsetc

__builtin_new_set_with_hash__
__builtin_new_set_with_compare__

def newset[t]([t])
	if t impl Comparable' -> __builtin_new_set_with_compare__(t.compare)
	else if t impl Hash'  -> __builtin_new_set_with_hash__(t.hash)
	else                  -> {t}
end

def newseth[t]([t])
	if t impl Hash'  -> __builtin_new_set_with_hash__(t.hash)
	else             -> {t}
end
```
### Map
A map is a collection of key-value pair, keys have no duplicate. A map type is identified by `{k:t}` where `k` is the the type of the key and `t` is the type of the value. It works exactly like a set but at any key it associates a value.
### Custom data types
A data type represents a memory block and the meaning of every portion of the block. Data types may have fields, each field has a type and a name and these are known at compile time. A data type is declared using the `data` keyword. Every data type has a constructor that describes the field.
```
data Person(name, last_name string, age int)
```
Creating an instance of a data type is done by using a syntax similar to python.
```
p = Person("nick", "kind", 21)
```
The `.` operator is used to read data fields.
```
name = p.name
```
Because of immutability in snack the only way to "update the state" of an instance is creating a copy and change only specific parameters.
```
p_grown = p(age=p.age+1)
```
### Partial constructor application
Because constructor are nothing more than functions, it's possible to use partial application.
```
data Person(name, last_name string, age int, job string)
```
The above data type describes a person, the following line shows how to partially apply a function to type a constructor that create a teacher.
```
def Teacher = Person(job="teacher")
```
## Traits
A trait defines the behavior of an object, it is a set of functions(methods) that act like a contract. If a data type adheres to the contract all the functions contained in the contract can be applied to that data type.
```
trait Hash'
	def hash int
end	
```
A data type that implements the `Hash'` trait must implement a method called `hash` that returns an `int` value. A trait can extends other traits, that is to say a trait A that extends a trait B has all the methods contained in trait A plus other methods.
```
trait Hash64' impl Hash'
	def hash64 int64
end
```
A data type that implements the `Hash64'` trait must implement both method `hash` and `hash64`. Traits can have function that work directly with the type that will implement the trait using the `for` keyword.
```
trait Equal' for t 
	def equal(t) boolean
	def notEqual(t) boolean
end
```
A trait can also have default implementation for its method.
```
trait Equal' for t 
	def equal(other t) boolean = not notEqual(other) 
	def notEqual(other t) boolean = not equal(other)
end
```
When the `Equal'` trait is implemented by a data type, if none of its method is implemented the computation will end in an infinite loop, but the developer only need to implement either `equal` or `notEqual` to implement the other one too.
The keyword `impl` allows a data type to implement a trait. The data type `Person(name, last_name string)` implements the `Equal'` as it follows. `@` qualify a field or a method and says that the code is referring to a part of the instance that is calling the method (in other programming languages is usually `self` or `this`).
```
impl Equal' for Person
	def equal(other Person) = @name==other.name and @last_name==other.last_name
end
```
A trait can be implemented when the data type is created.
```
data Person(name, last_name string) impl Equal'
	def equal(other Person) = @name==other.name and @last_name==other.last_name
end	
```
When implementing a trait int the data definition snack allows to create hidden fields to implement encapsulation. Hidden fields must be valued, because the standard constructor can not set a value. The methods can call an extended version of the constructor that includes hidden fields in the order they are declared.
```
trait Counter' for t
	def inc t
	def count int
end

data Counter impl Counter'
	counter = 0
	def count = @counter
	def inc = @(counter=@counter+1)
end
```
In the previous snippet the field `counter` is declared inside the body of the implementation and is not accessible outside the implementation, but inside the implementation block it is considered just like any other field. For increasing function decomposition the language let define private methods, that are normal methods but are not visible outside the implementation. Private methods are defined with `defp`, traits can not define private methods.

`.` is used for calling methods.
```
c = Counter
grown = c.inc
println!(grown.count) #1
```
A method can be called like an infix function in which the first parameter is an instance of data type.
```
p = Person("nicola", "gentile")
q = Person("nicola", "gentile")
cond = p equal q
neg_cond = p notEqual q
```
## Modules
A module is a set of functions, variables and data structure, it is a logic portion of code. It allows for code modularization and easy distribution of code. A module contains several functions and data types, but only those that are explicitly exported can be accessed outside the module. Each file is a module. A module may contains other modules, functions, variables, traits and custom data types.
A module is defined with ` module <module-name>(<list-of-exported-parama>) <module-content> end`.
Here is an example of the `math` module.
```
module math(sqrt, PI, geometry)

	def sqrt(x double) = #{code goes here}#

	PI = 3.14

	module geometry(Shape', Circle, Rectangle)

		trait Shape' 
			def area double
			def perimeter double
		end

		data Circle(r double) impl Shape'
			def area = r^2*PI
			def perimeter = 2*r*PI
		end

		data Rectangle(h, b double) impl Shape'
			def area = h*b
			def perimeter = 2*h*b
		end

	end

end
```

## Namespaces
A namespace is a part of the source code in which there can not be two symbols with the same name. A module is a namespace. A nested module is a separate namespace, the inner module and the outer module may contains symbol with the same name. A function is a namespace and the symbol defined in the function shadow the symbol defined in the outer namespace. Traits and data types are namespaces.
## Decision making



## Pattern matching
Pattern matching is the ability of a programming language to decompose an object or check if it has a particular shape.

### Pattern matching over assignment
When a variable get valued, under the hood the value of the right expression get binded to a variable, this is the most basic pattern matching example. Pattern matching allows to deconstruct objects.

#### Pattern matching over list
A list is composed by a head and a tail.
```
list = [1..=10]
head = list.head
tail = list.tail
```
Applying pattern matching over list can be done by the `::` operator.
```
[head | tail] = [1..=10]
```
It's allowed to concatenate more than one value.
```
[first, second | tail] = [1..=10]
```

#### Pattern matching over custom data types
A data type can be decomposed in its field.
```
data Point(x, y double)
Point(x, y) = Point(12.34, 56.78)
```

#### Pattern matching over strings
Pattern matching over string accomplished using regular expression. A regular expression is a set of characters that respect a specific syntax and that is delimited by ``.
```
captured = `([A..Za..z]+)\s([A..Za..z]+)`
```

#### Pattern matching over ranges
Pattern matching is an easy way to check whether a value lays in a range.
```
def upperCase?(c char) = c = 'A'..='Z'
def singleDigit?(x int) = c = 0..=9
def major?(p Person) = p = Person(_, _, 18..)
```

#### Pattern matching over sets and maps
```
set = newset(1, 2, 3, 4, 5, 6, 7, 8)
match set
	case {} do println("the set is empty")
	case {x} do println("is a singleton")
	case {1, 4, 7 | rest} do println("contains 1, 4, 7")
	otherwise println("something")
end
```
checking that a specific key is greater than or equal to 20
```
match map
	case {^key:20..|_} do println("map[$key] >= 20)
	case {^key:_|_} do println("map[$key] <= 20)
	otherwise println("map[$key] does not exist)
end
```
### Equality pattern matching
Most basic pattern matching is an equality check: if `n = 1` then `1 = n`. The `=` operator can be used  for checking if an object match a specific pattern.
```
data Person(name, lastName string, age int)
let p = Person("Nicola", "Gentile", 18)
if p = Person(_, _, 18..) -> println!("p is major")
else                      -> println!("p is minor")
```
### `match` operator
This operator checks if an object match a specific pattern.
```
def sum[t](list List[t]) List[t] = match list
	nil -> 0
	x:xs -> x + sum(xs)
end
```

### Pattern matching in assignment
```
data Point(x, y double)

main = do
	let p = Point(12.3, 45.6)
	Point(x, y) = p
	println("x=$x, y=$y")
end
```

### About pattern matching
When a pattern is not matched the program will crash in fact pattern matching can be used to check invariants in a program, usually what assert does in other programming languages. An example is the testing of the sum function.
```
def sum(a, b int) int = a + b + 1
2 = 1 sum 1 #the program will crash
```
## Functions as first class citizens
Being a functional language, snack's functions can be seen as instances like any other object, in fact they can be passed as parameter and can be returned.
##### Functions as parameter
To improve understanding of this concept we will show how to implement a function that do filter a list of int.
First of all we have to define the signature of the function. A function parameter is defined with the syntax `fn(<parameters-types>)<return-type>`.
```
def filter(list List[int], pred fn(int)bool) List[int]
```
Now we can use `pred` and `cons` just like normal function.
```
def filter(list List[int], pred fn(int)bool) = if [head | tail] = list and pred(head) 
	cons(head)
	doOnlyIf(tail)
end
```
``
The type of a function expected as parameter must be known at compile-time and is defined as `fn(<parameters-types>)<return-type>`.
Several kind of syntax are accepted by snack and each may be more or less convenient.

## Compiler macros
A macro is a compiler defined function that allows for compile level directives. The name of a macro starts with `$` and contains only capital letters and underscore. The language provides a set of macros, but any compiler can define its own set of macros. The compiler generates errors on macros only if a macro is not recognized and there is an error in the arguments, in case the macro is unknown the compiler will simply ignore it. The compiler let user to recall unknown macros to make the code understandable to many compilers, anyway the compiler may define some warnings that alert the user.
#### List of standard macros
- **$SET_PRIORITY_LEVEL(fun, num)**: set the priority level of the function `fun` at the level `num`, it must be specified in the same file where the function is declared. The level lays in the range 1 to 16
- **$PRIORITY_LEVEL(fun)**: read the priority level of a function
- **$SET_DEBUG_LEVEL(num)**: set the debug level to apply
- **$DEBUG_LEVEL()**: read the debug level
- **$SET_OPTIMIZATION_LEVEL(num)**: set the optimization level
- **$OPTIMIZATION_LEVEL()**: read the optimization level
- **$SET_SAFETY_ENABLED(boolval)**: set if safety mechanisms must be applied
- **$SAFETY_ENABLED()**: read if safety mechanisms are enabled
- **$INLINE(fun)**: force the compiler to inline a function, it must be specified in the same file where the function is declared.
- **$TARGET_PLATFORM()**: read the target platform

## Unsafe behavior

## Grammar
```
prog ::= def prog | [empty] 
def ::= <module-def> | <data-def> | <trait-def> | <impl-def> | <func-def> | <var-def>
module-def ::= module <module-id> <prog> end
module-id ::= WORD
data-def ::= data <data-id> <data-fields> <data-impl> <data-body>
data-id ::= WORD_A
data-fields ::= ( <data-fields-list> ) | [empty]
data-fields-list ::= <field-id> <opt-type>, <data-fields-list> | [empty]
field-id ::= WORD_AQ
opt-type ::= <type> | [empty]
type ::= <module-path> <type> | WORD_A <gen-args>
module-path ::= WORD. <module-path> | [empty]
gen-args ::= [<gen-type-list>]
gen-type-list ::= <gen-type> <gen-type-list-tail>
gen-type-list-tail ::= ,<gen-type> <gen-type-list-tail> | [empty]
gen-type ::= WORD_A <gen-type-impl>
gen-type-impl ::= impl <trait-list> | [empty]
trait-list ::= <trait-id> <trait-list-tail>
trait-id ::= WORD_A
trait-list-tail ::= & <trait-id> <trait-list-tail> | [empty]
var-def ::= <var-id> <opt-type> = <exp>
var-id ::= WORD_AQ
```

## Naming conventions
The following rules are not mandatory in order to make the code work but snack's creators adopted them to make code more clear:
- data types: UpperCamelCase, does not end with `'`.
- modules: snake_case
- functions: lowerCamelCase, no `?`,  `'` only at the end
- boolean functions: lowerCamelCase, `'` and `?` only at the end
- traits: UpperCamelCase, ends with `'`
- variables: snake_case