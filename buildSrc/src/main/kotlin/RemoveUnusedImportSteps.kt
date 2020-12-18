import com.github.autostyle.FormatterStep
import com.google.googlejavaformat.java.RemoveUnusedImports
import java.io.File

class FixedRemoveUnusedImportsStep : FormatterStep {

    override fun getName(): String = "removeUnusedImports"

    override fun format(rawUnix: String, file: File): String? {
        return RemoveUnusedImports.removeUnusedImports(rawUnix)
    }

}
