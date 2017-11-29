package code_generator

case class TypeSchema(name: String, namePlural: String, packageName: String, fields: Seq[Field]) {
  val className: String = name.capitalize
  val idClassName: String = s"${className}Id"
  val metaModelClassName: String = s"${className}MetaModel"

  // repo / db
  val repoClassName: String = s"${className}Repo"
  val dbTableClassName: String = s"${className}Table"
  val tableName: String = namePlural

}