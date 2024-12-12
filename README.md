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
sum(a int, b int) int = a + b
```
The compiler is able to infer the result's type, in fact it's not mandatory do explicit it. The above function can be written as
```
sum(a int, b int) = a + b
```
The type of an argument may be omitted, the compiler will consider the type of the successive parameter. Last argument's type must be explicitly defined.
```
sum(a, b int) = a + b
```
Functions may have default parameter that is to say if a parameter value is not specified its value will be the default value.  There can not be a normal parameter after a default parameter.
```
add(n, inc int) = n + inc
```
A function with no argument still needs parenthesis.
```
half() = 1/2
```
A function may have no return type like a function that print a message on the standard output.
```
sayHelloWorld() = println("hello world!!")
```
The function identifier have to start with a letter and may contains letters, numbers, `_`, `?`, `'` and may end with `!`. Functions that end with `!` are unsafe functions, therefore can not be called by a pure function, other details about unsafe functions are given in the [dedicated section](#unsafe-behavior).
### Function call
A function call is like reusing the procedure described by the function. These procedures are parameterized. A function call is made of the name of the function and the list of the parameters enclosed in parentheses. In the following line of code it's declared a variable s that is the difference between 2 and 3.
```
sub(a, b int) int = a - b
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
add(a, b int) = a + b
mul(a, b int) = a * b

add.$priorityLevel = 7
mul.$priorityLevel = 8
```
### Partial function application
When there is the need of calling a functions always with the same parameter `snack` provides a sugar syntax to makes code cleaner. Partial function application consist of creating a copy of the function with some parameters set. An example is a multiplication function `multiply`. This function takes two numbers and return their product. Might happen that a program needs several multiplication by 2, without partial application the implementation would look like
```
multiply(a, b int) = a * b
multiplyBy2 = multiply(2)
```
To understand what happens, the compiler will generate the function
```
multiplyBy2(a int) = multiply(2, a)
```
Partial application can be applied even if the parameters order gets changed.
```
divide(dividend, divisor double) = a / b
divideBy2 = divide(divisor=2.0)
divideBy2' = (divide 2.0)
```
### Expressions
An expression is a computation sequence that have a result. There are several type of expressions:
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
Are expressions that uses  `&`, `|`, `~`, `@`
```
#implementation of not(a or b) and (a xor b)
boolexp(a, b boolean) = ~(a|b)&(a@b)
```
Boolean expression operators ha
#### Literal values
Literal values are values that can  be evaluated at compile time and have the type of the
#### Function call
A function call is like reusing the procedure described by the function. These procedures are parameterized. A function call is made of the name of the function and the list of the parameters enclosed in parentheses. In the following line of code it's declared a variable s that is the difference between 2 and 3.
```
sub(a, b int) int = a - b
s = sub(5, 3) #2
```
In the above snippet the arguments are passed by position, it means that the first value refers to the first argument and so on. Snack allows passing values by name making explicit the value of a parameter, just like an assignment.
```
ss = sub(3, 5) #-2
s = sub(b=3, a=5) #2
```
It's possible to pass some argument by name and some by position, in that case the relative positional arguments order must be respected.
##### Infix function
Functions with just 2 parameters can be used as an operator: `sub(2, 3)` is equivalent to `2 sub 3`. Unlike operators there is no priority between infix function, they are evaluated from left to right.
```
mul(a, b int) = a * b
sum(a, b int) = a + b
x = 2 sum 3 mul 4 #(2 sum 3) mul 4 = 20
y = 3 mul 4 sum 2 #(3 mul 4) sum 2 = 14
```
#### do-block
A do-block is a set of instruction that will be evaluate to single expression and the final result correspond to the result of the last expression. In fact when a function is composed of multiple line of code they must be enclosed in a do block.
```
printSum(a, b int) = do
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
A literal character is defined a symbol enclosed in single quotes. Some special characters are encoded as pair of two characters, these are called escape characters, they are all listed in the following table.

| pair | encoded character |
| ---- | ----------------- |
| \n   | new line          |
| \r   | carriage return   |
| \t   | tabulation        |
| \\\\ | back slash        |
| \b   | backspace         |
| \f   | form feed         |
| \'   | apostrophe        |    
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
A list can be constructed in several ways, using list comprehension.
```
l1 = [0..10] #all numbers from 0 to 9
l2 = [0..=10] #all numbers from 0 to 10
l3 = [i*2 for i=0..10] #all numbers from 0 to 9 but doubled
l4 = [i*2 for i=0..=10 if i.even?] #all even numbers from 0 to 10 but doubled
```
It supports no operation by default but the standard library implements all the necessary methods, to work with list is necessary to use pattern matching.

### Set
A set is a collection of items of the same types with no duplicate. It is identified by `{t}` where `t` is the type of the objects it contains. By default the set uses a compiler generated hash function for storing objects, but this methods has some limits. Snack provides two builtin function for supplying a custom hash function or a compare function.
```
data Person(name, last_name string, age int) impl Hash'
	hash() = name.hash + last_name.hash
end

hashset = newhashset(Person.hash)
set = newset

__builtin_new_set_with_hash__
__builtin_new_set_with_compare__

newset[t]([t])
	if t impl Comparable' -> __builtin_new_set_with_compare__(t.compare)
	else if t impl Hash'  -> __builtin_new_set_with_hash__(t.hash)
	else                  -> {t}
end

newseth[t]([t])
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
Teacher() = Person(job="teacher")
```
## Traits
A trait defines the behavior of an object, it is a set of functions(methods) that act like a contract. If a data type adheres to the contract all the functions contained in the contract can be applied to that data type. Methods defined in traits must explicitly specify the return type.
```
trait Hash'
	hash() int
end	
```
A data type that implements the `Hash'` trait must implement a method called `hash` that returns an `int` value. A trait can extends other traits, that is to say a trait A that extends a trait B has all the methods contained in trait A plus other methods.
```
trait Hash64' impl Hash'
	hash64() int64
end
```
A data type that implements the `Hash64'` trait must implement both method `hash` and `hash64`. Traits can have function that work directly with the type that will implement the trait using the `for` keyword.
```
trait Equal' for t 
	equal(t) boolean
	notEqual(t) boolean
end
```
A trait can also have default implementation for its method.
```
trait Equal' for t 
	equal(other t) = not notEqual(other) 
	notEqual(other t) = not equal(other)
end
```
When the `Equal'` trait is implemented by a data type, if none of its method is implemented the computation will end in an infinite loop, but the developer only need to implement either `equal` or `notEqual` to implement the other one too.
The keyword `impl` allows a data type to implement a trait. The data type `Person(name, last_name string)` implements the `Equal'` as it follows. `$` qualify a field or a method and says that the code is referring to a part of the instance that is calling the method (in other programming languages is usually `self` or `this`).
```
impl Equal' for Person
	equal(other Person) = $name==other.name and $last_name==other.last_name
end
```
A trait can be implemented when the data type is created.
```
data Person(name, last_name string) impl Equal'
	equal(other Person) = $name==other.name and $last_name==other.last_name
end	
```
When implementing a trait int the data definition snack allows to create hidden fields to implement encapsulation. Hidden fields must be valued, because the standard constructor can not set a value. The methods can call an extended version of the constructor that includes hidden fields in the order they are declared.
```
trait Counter' for t
	inc() t
	count() int
end

data Counter impl Counter'
	counter = 0
	count() = $counter
	inc() = $(counter=$counter+1)
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

	sqrt(x double) = #{code goes here}#

	PI = 3.14

	module geometry(Shape', Circle, Rectangle)

		trait Shape' 
			area() double
			perimeter() double
		end

		data Circle(r double) impl Shape'
			area() = r^2*PI
			perimeter() = 2*r*PI
		end

		data Rectangle(h, b double) impl Shape'
			area() = h*b
			perimeter() = 2*h*b
		end

	end

end
```

## Namespaces
A namespace is a part of the source code in which there can not be two symbols with the same name. A module is a namespace. A nested module is a separate namespace, the inner module and the outer module may contains symbol with the same name. A function is a namespace and the symbol defined in the function shadow the symbol defined in the outer namespace. Traits and data types are namespaces.
## Decision Making
Like any other language the flow is controlled by conditional statements and conditional expression. Conditional statements allows to execute or not a part of the program either a condition is true or false, the last expression must not return any result. Conditional expressions are expressions where the computation of the result depends on one or more conditions. The main difference is that conditional expression must evaluate to something, possible condition must be handled.
### if-else statement
The if-else execute a block of code if the provided condition is true. Condition expressions must evaluate in a boolean expression, if it does not the compiler will raise an error. The block of instruction to be executed when a condition is true must be closed with `end`.
```
n = 2
m = 3
if n == 2 
	print("n equal 2")
end
if n == 3
	print("n equal 3")
else
	print("n not equal 3")
end
if n == m 
	print("n == m")
else if n < m
	print("n < m")
else
	print("n > m")
end
```
The set of instruction of a conditional branch must evaluate in an expression with no result otherwise the compiler will consider the statement a conditional expression.
We can us if-else to choose how to compute a value branching the flow of the program.
```
n = 2
month =
	if n == 1
		"Genuary"
	else if n == 2
		"February"
	else 
		"AnyOtherMonth"
	end
```
To make the code more clear the following syntax is also valid.
```
n = 2
month = 
	if n == 1      -> "Genuary"
	else if n == 2 -> "February"
	else           -> "AnyOtherMonth"
```


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

#### Pattern matching over ranges
Pattern matching is an easy way to check whether a value lays in a range.
```
upperCase?(c char) = c = 'A'..='Z'
singleDigit?(x int) = c = 0..=9
major?(p Person) = p = Person(_, _, 18..)
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
valueGreaterThan20(key string) = match map
	case {^key:20..|_} do println("map[$key] >= 20)
	case {^key:_|_} do println("map[$key] < 20)
	otherwise println("map[$key] does not exist")
end
```
### Equality pattern matching
Most basic pattern matching is an equality check: if `n = 1` then `1 = n`. The `=` operator can be used  for checking if an object match a specific pattern.
```
data Person(name, lastName string, age int)
let p = Person("Nicola", "Gentile", 18)
if p = Person(_, _, 18..) do println!("p is major")
else                      do println!("p is minor")
```
### `match` operator
This operator checks if an object match a specific pattern.
```
sum[t](list List[t]) List[t] = match list
	case [] do 0
	case [x | xs] do x + sum(xs)
end
```
`match` is useful to write clean sequences of if-else statement.
```
n = 45
if n < 0 do
	println("n is negative")
else if n < 10 do
	println("n is a one digit number")
else if n < 100 do
	println("n is a 2 digit number")
else
	println("n is a 3 or more digit number")
end
```
The above snippet can be rewritten with `match`.
```
let n = 45
match n
    case ..0 do println("n is a 3 or more digit number")
    case ..10 do println("n is a one digit number")
    case ..100 do println("n is a 2 digit number")
    else println("n is a 3 or more digit number")
end
```
### Pattern matching in assignment
```
data Point(x, y double)

main() = do
	let p = Point(12.3, 45.6)
	Point(x, y) = p
	println("x=$x, y=$y")
end
```

### About pattern matching
When a pattern is not matched the program will crash in fact pattern matching can be used to check invariants in a program, usually what assert does in other programming languages. An example is the testing of the sum function.
```
sum(a, b int) = a + b + 1
2 = 1 sum 1 #the program will crash
```
## Sum data type
An object of the same type can assume different shape, these are called sum types. A sum type is defined using `|` to separate each shape. As an example geometric shapes are perfect.
```
trait Shape'
    area() double
    perimeter() double
end

data Shape = Rectangle(width, height double) | Circle(radius double) impl Shape'
    area() = match $
        case Reactangle(w, h) do w*h
        case Circle(r) do r^2*PI
    end
    perimeter() = match $
        case Reactangle(w, h) do (w+h)*2
        case Circle(r) do r*2*PI
    end
end
```
## Functions as first class citizens
Being a functional language, snack's functions can be seen as instances like any other object, in fact they can be passed as parameter and can be returned.
##### Functions as parameter
To improve understanding of this concept we will show how to implement a function that do filter a list of int.
First of all we have to define the signature of the function. A function parameter is defined with the syntax `fn(<parameters-types>)<return-type>`.
```
filter(list [int], pred fn(int)bool) List[int]
```
Now we can use `pred` just like any other function.
```
filter(list List[int], pred fn(int)bool) = match list 
    case [head | tail] do
        if pred(head) do head :: filter(tail)
        else filter(tail)
        end
    case [] do 
        []
    end
```
The type of a function expected as parameter must be known at compile-time and is defined as `fn(<parameters-types>)<return-type>`.
Several kind of syntax are accepted by snack to pass a function as argument.
##### Pass an existing function
If we'd like to print the doubled values we should write something like:
```
mulOf3(x int) = x % 3 == 0

main() = do
	list = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	filter(list, mulOf3)
end	
```
##### Lambda functions
Lambda functions are functions that can be created in place. If the function to pass as argument does not exist yet, it can be created directly in the function call, like a literal character.
###### Lambda syntax
```
filter([1..=10], \n -> n%3=0)
```
###### Counter syntax
```
filter([1..=10], $0%3 = 0)
```
###### Implicit syntax
```
filter([1..=10], _%3 = 0)
```
## Objects metadata
Any entities in snack has metadata. Entities are variables, functions, modules, data types and traits. Accessing metadata can be done by the usual attribute access syntax with `.` operator. Metadata are divided in two groups, readonly and assignable metadata. Assignable metadata can be valued only once (otherwise they have default values). A metadata is named as any other identifier but it must start by `$`.
### Variables metadata
#### $name
#### $type
#### $size
### Functions metadata
#### $name
#### $retType
#### $params
#### $size
#### $priorityLevel
#### $inline
#### $localVars
### Modules metadata
#### $name
#### $constants
#### $functions
#### $modules
#### $datatypes
#### $traits
#### $file

### Data types metadata

## Unsafe behavior

## Grammar
```
module-id ::= [A-Za-z_]+
data-id ::= [A-Za-z_][A-Za-z_0-9'?]*
trait-id ::= [A-Za-z_][A-Za-z_0-9'?]*
var-id ::= [A-Za-z_][A-Za-z_0-9'?]*
world-fun-id ::= [A-Za-z_][A-Za-z_0-9'?]*!?
symbol-fun-id ::= [+\-*/<>|~^&=%$]+
fun-id ::= <world-fun-id> | <symbol-fun-id>

prog ::= def+
def ::= <module-def> | <data-def> | <trait-def> | <impl-def> | <var-def> | <fun-def>

module-def ::= `module` <module-id> (`(`<id-list>?`)`)? <prog> end
id-list ::= <id> (`,`<id>)*
id ::= <module-id> | <data-id> | <trait-id> | <var-id> | <fun-id>

var-def ::= <var-id> <type>? `=` <exp>
part-fun-sign ::= <world-fun-id> `(` <param-list>? `)`
fun-sign ::= <part-fun-sign> <type>
fun-def ::= <part-fun-sign> <type>? `=` <exp>

data-def ::= `data` <data-id> <gen-args>? (<sum-types>|`(`<param-list>`)`) <data-impl>?
gen-args ::= `[`<gen-type> (`,` <gen-type>)*`]`
gen-type ::= <data-id> <impl>?
impl ::= `impl` <trait-id> (`&` <trait-id>)*
param-list ::= <param> (`,` <param>)*
param ::= <var-id> <type>?
sum-types ::= <sum-type> (`|` <sum-type>)*
sum-type ::= <data-id> `(` <param-list> `)`
data-impl ::= <impl> (<fun-def> | <var-def>)+ `end`

trait-def ::= `trait` <trait-id> <gen-args> <impl> (<fun-sign> | <fun-def>)+ `end`

impl-def ::= `impl` <type> for <trait-id> (`&` <trait-id>)* <fun-def>+ `end`

type ::= <list-type> | <set-type> | <map-type> | (<module-id>`.`)* <type-id> (`[`<type> (`,` <type>)*]`)?
type-id ::= <data-id> | <trait-id>
list-type ::= `[`<type>`]`
set-type ::= `{`<type>`}`
map-type ::= `{`<type>`:`<type>`}`

exp ::= <fun-call> | <infix-fun-call> | <range-exp> | <match-exp> | <if-exp> | <do-block> | <var-def> | <fun-def> | <list-exp> | <set-exp> | <map-exp>

fun-call ::= (<module-id>`.`)* (<var-id>`.`)* <world-fun-id>(`[`<type>(`,`<type>)`]`)? (`(`<params-list>`)`)?
params-list ::= <param> (`,` <param>)
param ::= (<var-id>`=`)?<exp> 

infix-fun-call ::= <exp> <fun-id> <exp>

range-exp ::= <exp>`..`(`=`?<exp>)?

match-exp ::= `match` <exp> <case-exp>+ <else-case.exp>? `end`
case-exp ::= `case` <match-case> `do` <exp>+
match-case ::= `^`?<var-id> | <number-match-case> | <list-match-case> | <set-match-case> | <map-match-case> | <str-match-case> | <range-match-case> | <data-match-case> | `_`
number-match-case ::= <integer> | <real>
list-match-case ::= `[` (<match-case>(`,`<match-case>)*(`|` <var-id>)?)? `]`
set-match-case ::= `{` (<match-case>(`,`<match-case>)*(`|` <var-id>)?)? `}`
map-match-case ::= (<match-case>`:`<match-case>(`,`<match-case>`:`<match-case>)*(`|` <var-id>)?)? `}`
str-match-case ::= <str>
range-match-case ::= (<exp>`..`(`=`?<exp>)?) | `..``=`?<exp>
data-match-case ::= <data-id>`(`(<param-match-case>(`,`<param-match-case>))?`)`
param-match-case ::= (<var-id>`=`)?<match-case>
else-case-exp ::= `else` <exp>+

if-exp ::= `if` <exp> `do` <exp>+ <if-tail>
if-tail ::= `else` <else-tail>
else-tail ::= <if-exp> | <exp>+ `end`

do-block ::= `do` <exp>+ `end`

list-exp ::= `[`<comprehension-exp>|<exp-list>?`]`
comprehension-exp ::= <exp> (`for` <var-def>)? (`if` <exp>)?
exp-list ::= <exp>(`,` <exp>)*

set-exp ::= `{`<comprehension-exp>|<exp-list>?`}`

map-exp ::= `{`<kv-pair-list>?`}`
kv-pair-list ::= <kv-pair>, (`,` <kv-pair>)*
kv-pair ::= <exp>`:`<exp>
```

## Naming conventions
The following rules are not mandatory in order to make the code work but snack's creators adopted them to make code more clear:
- data types: UpperCamelCase, does not end with `'`.
- modules: snake_case
- functions: lowerCamelCase, no `?`,  `'` only at the end
- boolean functions: lowerCamelCase, `'` and `?` only at the end
- traits: UpperCamelCase, ends with `'`
- variables: snake_case