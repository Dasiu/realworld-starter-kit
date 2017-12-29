package core.users.repositories

import commons.models.{Email, IdMetaModel, Property, Username}
import commons.repositories._
import commons.repositories.mappings.JavaTimeDbMappings
import core.users.models.{User, UserId, UserMetaModel}
import slick.dbio.DBIO
import slick.jdbc.MySQLProfile.api.{DBIO => _, MappedTo => _, Rep => _, TableQuery => _, _}
import slick.lifted.{ProvenShape, _}

import scala.concurrent.ExecutionContext

class UserRepo(override protected val dateTimeProvider: DateTimeProvider,
               implicit private val ex: ExecutionContext)
  extends BaseRepo[UserId, User, UserTable]
      with AuditDateTimeRepo[UserId, User, UserTable] {

  def byEmail(email: Email): DBIO[User] = {
    require(email != null)

    query
      .filter(_.email === email)
      .result
      .headOption
      .map(_.get)
  }

  def byUsername(username: Username): DBIO[Option[User]] = {
    require(username != null)

    query
      .filter(_.username === username)
      .result
      .headOption
  }

  override protected val mappingConstructor: Tag => UserTable = new UserTable(_)

  override protected val modelIdMapping: BaseColumnType[UserId] = UserId.userIdDbMapping

  override protected val metaModel: IdMetaModel = UserMetaModel

  override protected val metaModelToColumnsMapping: Map[Property[_], (UserTable) => Rep[_]] = Map(
    UserMetaModel.id -> (table => table.id),
    UserMetaModel.username -> (table => table.username)
  )

  implicit val usernameMapping: BaseColumnType[Username] = MappedColumnType.base[Username, String](
    username => username.value,
    str => Username(str)
  )
}

protected class UserTable(tag: Tag) extends IdTable[UserId, User](tag, "users")
  with AuditDateTimeTable
  with JavaTimeDbMappings {

  def username: Rep[Username] = column[Username]("username")

  def email: Rep[Email] = column[Email]("email")

  def * : ProvenShape[User] = (id, username, email, createdAt, updatedAt) <> ((User.apply _).tupled, User.unapply)
}