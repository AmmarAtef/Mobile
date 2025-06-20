fun main() {
    // Book Object
    val book = Book("The Alchemist", "Paulo Coelho", 14.99)
    book.read()
    println("Title: ${book.getTitle()}, Author: ${book.getAuthor()}, Price: \$${book.getPrice()}")
    book.setPrice(12.99)
    println("Updated Price: \$${book.getPrice()}")

    println("-----------")

    // EBook Object
    val ebook = EBook("Digital Fortress", "Dan Brown", 9.99, "PDF")
    ebook.read()
    println("Title: ${ebook.getTitle()}, File Type: ${ebook.getFileType()}")
    ebook.setFileType("ePub")
    println("Updated File Type: ${ebook.getFileType()}")
}
