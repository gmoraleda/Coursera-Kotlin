package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

class SquareBoardImpl(override val width: Int): SquareBoard {

    private val cells = (1..width)
            .flatMap { i ->
                (1..width)
                        .map { j ->
                            Cell(i, j)
                        }
            }


    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return cells.find { cell -> cell.i == i && cell.j == j }
    }

    override fun getCell(i: Int, j: Int): Cell {
        return cells.find { cell -> cell.i == i && cell.j == j } ?: throw IllegalArgumentException()
    }

    override fun getAllCells(): Collection<Cell> {
        return cells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val sortedCells = mutableListOf<Cell?>()
        for (j in jRange) {
            sortedCells += getCellOrNull(i, j)
        }
        return sortedCells.filterNotNull()
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val sortedCells = mutableListOf<Cell?>()
        for (i in iRange) {
            sortedCells += getCellOrNull(i, j)
        }
        return sortedCells.filterNotNull()
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when(direction) {
            UP -> getCellOrNull(i-1, j)
            DOWN -> getCellOrNull(i+1,j)
            LEFT -> getCellOrNull(i, j-1)
            RIGHT -> getCellOrNull(i,j+1)
        }
    }
}
fun <T> createGameBoard(width: Int): GameBoard<T> = TODO()

