package androidkotlin.training.city

// On vas cree une class city ayant les attributs id & name

data class City(
    var id: Long,
    var name: String) {
    constructor(name: String) : this(-1, name)
}