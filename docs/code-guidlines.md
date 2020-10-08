## Code guidlines and HOWTOs

### Handling Excepitons

We recommend following these rules when dealing with exceptions in MyLibreLab:

* Avoid empty catch blocks at all costs, if an empty catch block is intentional, make it clear with variable name and comment.
* Catch specific exceptions instead of the more general ones. Catching `Throwable` should never be done, and catching `Exception` only if necessary. If there is more than one expected exception, catch them all individually instead of catching `Exception`.
* Do not catch `NullPointerException`.
* Log every exception with `org.tinylog.Logger`.
* Close every resource with _try-with-resources_.
* Avoiding null checks that will hide bugs. Fail fast. `NullPointerException` is much more preferred than method failing to execute silently.
* When there is new exception being thrown inside the catch block we prefer sending the original (causal) exception with the new one. More precisely, `constructor Exception(String message, Throwable cause)`.
* Every exception should have a message describing what caused it.
