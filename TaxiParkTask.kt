package taxipark

import kotlin.math.roundToInt

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        (allDrivers - trips.map { it.driver }).toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        if (minTrips > 0) trips
                .flatMap { trip -> trip.passengers }
                .groupBy { passenger -> passenger }
                .filterValues { group -> group.size >= minTrips }
                .keys
        else allPassengers

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        trips
                .filter { it.driver == driver }
                .flatMap { it.passengers }
                .groupBy { it }
                .filterValues { it.size > 1 }.keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        trips.filter { it.discount != null }
                .flatMap { it.passengers }
                .groupBy { it }
                .filter { it ->
                    val withoutDiscount = trips
                            .filter { it.discount == null }
                            .flatMap { it.passengers }
                            .groupBy { it }
                    val trips = withoutDiscount[it.key]
                    val count =  if (trips != null) trips.count() else 0
                    count < it.value.count()
                }.keys

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val durations = trips.map { it.duration }
    val map = durations.map {
        val min = it%10
        IntRange(it-min, it-min+9) to it
    }.groupBy { it.first }
    return map.maxBy { it.value.count() }?.key }

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false
    val numberOfTopDrivers = (allDrivers.size * 0.2).toInt()
    val earnings = trips.map { it.cost }.reduce { acc, d -> acc + d }
    val driversMap = trips.map { it.driver to it.cost }.groupBy { it.first }.map { it.key to it.value.sumByDouble { it.second } }.sortedByDescending { it.second }
    val topDriversSlice = driversMap.subList(0,numberOfTopDrivers)

    return topDriversSlice.sumByDouble { it.second } >= earnings * 0.8
}
