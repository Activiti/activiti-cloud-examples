If you want to contribute get in touch with the project members and ask for guidance. 
This project is a community driven project, so get in touch if you want to participate. 

Unit tests
--------------------
Activiti uses now [AssertJ](http://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html) 
as alternative to default JUnit assertions. This makes tests more readable and provides clearer failure messages. 
Please, make sure that you are using `AssertJ` assertions for every new test.
I.e.
* `assertThat(myBoolean).isTrue();` instead of `assertTrue(myBoolean);`
* `assertThat(actual).isEqualTo(expected);` instead of `assertEquals(expected, actual);`
* `assertThat(myList).containsExactly("a", "b", "c");` instead of `assertTrue(myList.contains("a", "b", "c"));`

To easily differentiate between unit and integration tests the following naming convention should be used:
 * a `unit test` java class should end with `*Test.java`
 * an `integration test` java class should end with `*IT.java`
 
This will help if we want to be able to skip either unit tests or integration tests.

Branches naming convention
--------------------------
If you are creating branches directly in the [main repository](https://github.com/Activiti/Activiti), this is 
 the naming convention to be used for branches:
 - `username-issueNumber-anythingYouWant`
