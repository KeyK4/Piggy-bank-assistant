package Models

class PatternIncome(id_: Long, percentage_: Int) {
    val Id: Long
    val Percentage: Int
    init {
        Id = id_
        Percentage = percentage_
    }
}