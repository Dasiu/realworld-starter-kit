package code_generator

import java.io.{File, FileWriter}

import play.twirl.api.Template1

class Generator(val fileNameSuffix: String, template: Template1[TypeSchema, play.twirl.api.TxtFormat.Appendable]) {

  def generate(typeSchema: TypeSchema): String = {
    template.render(typeSchema).toString()
  }

}

object Generator {
  val generators: Seq[Generator] = Seq(
    new Generator("", twirl.txt.Entity),
    new Generator("Repo", twirl.txt.Repo)
  )
}

object CodeGeneratorApp extends App {
  val typeSchema = TypeSchema("Comment", "comments", "core.articles.models", List(
    Field("id", "CommentId"),
    Field("content", "String"),
  ))

  Generator.generators.foreach(generator => {
    val code = generator.generate(typeSchema)

    val filename = s"${typeSchema.className}${generator.fileNameSuffix}"
    val templateInstanceFile = new File(s"d:\\\\$filename.scala")
    var templateInstance: FileWriter = null
    try {
      templateInstance = new FileWriter(templateInstanceFile)
      templateInstance.write(code)

    } finally {
      templateInstance.close()
    }
  })

}
