open class Book(
    private var title: String,
    private var author: String,
    private var price: Double
) {

    fun getTitle(): String = title
    fun getAuthor(): String = author
    fun getPrice(): Double = price

    fun setTitle(newTitle: String) {
        title = newTitle
    }

    fun setAuthor(newAuthor: String) {
        author = newAuthor
    }

    fun setPrice(newPrice: Double) {
        price = newPrice
    }

    open fun read() {
        println("Reading Paper book: \"$title\" by $author")
    }
}
