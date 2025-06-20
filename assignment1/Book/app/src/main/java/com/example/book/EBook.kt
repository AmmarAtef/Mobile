class EBook(
    title: String,
    author: String,
    price: Double,
    private var fileType: String
) : Book(title, author, price) {

    fun getFileType(): String = fileType

    fun setFileType(newType: String) {
        fileType = newType
    }

    override fun read() {
        println("Read from Electronic Device: \"${getTitle()}\" in $fileType format")
    }
}
