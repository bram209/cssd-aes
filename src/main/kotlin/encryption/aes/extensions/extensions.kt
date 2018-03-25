package encryption.aes.extensions


fun <T> Array<T>.asCyclingSequence(): Sequence<T> = Sequence { this.asIterable().cyclingIterator() }

fun <T> Iterable<T>.cyclingIterator(): Iterator<T> {
    return object: Iterator<T> {
        var iter = this@cyclingIterator.iterator()

        override fun hasNext(): Boolean {
            if (!iter.hasNext()) {
                iter = this@cyclingIterator.iterator()
            }

            return iter.hasNext()
        }

        override fun next(): T {
            if (!hasNext()) {
                throw NoSuchElementException("Can't cycle through an empty iterator")
            }

            return iter.next()
        }
    }
}