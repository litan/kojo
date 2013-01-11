import com.kogics.scrisca.eclipse.CpXtractor
val jars = CpXtractor.extract("/home/lalit/work/KojoDev/KojoLite").split(";")
clearOutput()
jars foreach (println _)