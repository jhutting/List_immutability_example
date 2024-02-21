# Immutability in Java records using collections

Java records are a great move towards immutable objects without having to rely on Lombok's @Value or moving to another language like Kotlin (if you haven't developed in it, please do - not just to learn the ups and downs of another language, but also to appreciate the improvements Java received the past years).

This won't be a deep dive into records, José Paumard with [Clean Code with Records, Sealed Classes and Pattern Matching](https://www.youtube.com/watch?v=Zc6vkps6ZEM), Piotr Przybył with [Java Records for the Intrigued](https://www.youtube.com/watch?v=Ct-mjCmODzs) and Nicolai Parlog with [Java 21💣💥](https://www.youtube.com/watch?v=uNeV3EBUS18) already offer a great insight on them.

However, records containing a reference to a mutable object such as an ArrayList can still be updated. This example was initially triggered by a
[Twitter post](https://twitter.com/eliasnogueira/status/1759631950279561350) by Elias Nogueira on combining mutable Builders with the new Java record where Piotr was joking with ```record Record(ArrayList<String> gimmeMutabilityBack) {}```. As I prefer runnable code to discuss a subject I decided to write a small follow-up (that I might turn into a small blog post or article as well).

**n.b.** This is not a specific issue with the record type or Lists, but a bigger challenge there as you expect the fields of a record to be immutable.

## List immutability in records
Combining a record with a builder for default values or scaffolding until you're ready to make the contents immutable is an interesting concept IMHO, so I will cover them alongside the normal instantiation.

Yet using a mutable List (such as ArrayList) and using ```Collections.unmodifiableList(source)``` has a pitfall: while in the latter case the newly created list is unmodifiable and will throw an ```UnsupportedOperationException``` when you add/update/delete items, you can still add items to the source list which is reflected in the outcome when you call the contents of the record.

The best way to achieve immutability seems to be ```List.copyOf(source)```, as every ```.buildWithCopyOf()``` will result in a fully immutable record. If you have any better suggestions - let me know.

Example testcases:
* Using ```List.copyOf(source)``` [With builder](https://github.com/jhutting/List_immutability_example/blob/main/src/test/java/io/github/jhutting/examples/record/immutablelist/MutableRecordTest.java?plain=1#L132) and [without](https://github.com/jhutting/List_immutability_example/blob/main/src/test/java/io/github/jhutting/examples/record/immutablelist/MutableRecordTest.java?plain=1#L69) being immutable
* USing ```Collections.unmodifiableList(source)``` [With builder](https://github.com/jhutting/List_immutability_example/blob/main/src/test/java/io/github/jhutting/examples/record/immutablelist/MutableRecordTest.java?plain=1#L109) and [without](https://github.com/jhutting/List_immutability_example/blob/main/src/test/java/io/github/jhutting/examples/record/immutablelist/MutableRecordTest.java?plain=1#L49) being mutable through the source List object, while the record field does not allow you to adjust it directly
* USing ```a mutable ArrayList``` [With builder](https://github.com/jhutting/List_immutability_example/blob/main/src/test/java/io/github/jhutting/examples/record/immutablelist/MutableRecordTest.java?plain=1#L91) and [without](https://github.com/jhutting/List_immutability_example/blob/main/src/test/java/io/github/jhutting/examples/record/immutablelist/MutableRecordTest.java?plain=1#L34) being mutable through the gimmeMutabilityBack ArrayList supplied to the record
