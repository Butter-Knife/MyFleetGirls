package models

import scalikejdbc.SQLInterpolation._
import scalikejdbc.{DBSession, WrappedResultSet}
import util.scalikejdbc.BulkInsert._

/**
 *
 * @param deckId : 艦隊No.
 * @param num : 順番（旗艦0）
 */
case class DeckShip(deckId: Int, num: Int, memberId: Long, shipId: Int)

object DeckShip extends SQLSyntaxSupport[DeckShip] {
  def apply(x: SyntaxProvider[DeckShip])(rs: WrappedResultSet): DeckShip = apply(x.resultName)(rs)
  def apply(x: ResultName[DeckShip])(rs: WrappedResultSet): DeckShip = new DeckShip(
    rs.int(x.deckId),
    rs.int(x.num),
    rs.long(x.memberId),
    rs.int(x.shipId)
  )

  lazy val ds = DeckShip.syntax("ds")

  def create(deckId: Int, num: Int, memberId: Long, shipId: Int)(
    implicit session: DBSession = DeckShip.autoSession): DeckShip = {
    applyUpdate {
      insert.into(DeckShip).namedValues(
        column.deckId -> deckId, column.num -> num, column.memberId -> memberId, column.shipId -> shipId
      )
    }
    DeckShip(deckId, num, memberId, shipId)
  }

  def bulkInsert(deckId: Int, memberId: Long, ships: List[Int])(
    implicit session: DBSession = DeckShip.autoSession): List[DeckShip] = {
    require(ships.nonEmpty)
    deleteByDeck(deckId, memberId)
    applyUpdate {
      insert.into(DeckShip)
        .columns(column.deckId, column.num, column.memberId, column.shipId)
        .multiValues(Seq.fill(ships.size)(deckId), Seq.range(0, ships.size), Seq.fill(ships.size)(memberId), ships)
    }
    ships.zip(Stream.from(0)).map { case (ship, num) =>
      DeckShip(deckId, num, memberId, ship)
    }
  }

  def deleteByDeck(deckId: Int, memberId: Long)(
    implicit session: DBSession = DeckShip.autoSession): Unit = applyUpdate {
    delete.from(DeckShip)
      .where.eq(DeckShip.column.memberId, memberId).and.eq(DeckShip.column.deckId, deckId)
  }

  def deleteByUser(memberId: Long)(
    implicit session: DBSession = DeckShip.autoSession): Unit = applyUpdate {
    delete.from(DeckShip)
      .where.eq(DeckShip.column.memberId, memberId)
  }
}
