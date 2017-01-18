package mazine.plants

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.FileReader
import java.io.FileWriter
import java.util.*

fun main(args: Array<String>) {
    val csvFormat = CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader()

    val csvParser = csvFormat.parse(FileReader("/Users/mazine/Downloads/customer-list.csv"))


    val customers = LinkedHashMap<String, Customer>()
    csvParser.records.forEach {
        val instanceName = it["Instance"].trim()
        val email = it["Admin Email"].trim()
        val username = it["Compromised Account"].trim()

        val customer = customers[email] ?: Customer(email).apply {
            customers[email] = this
        }

        val instance = customer.instances[instanceName] ?: Instance(instanceName).apply {
            customer.instances[instanceName] = this
        }

        instance.usernames.add(username)
    }


    val csvPrinter = CSVPrinter(FileWriter("grouped-customer-list.csv"), csvFormat)
    csvPrinter.printRecord("Email", "Instance(s)", "Instance names", "Account(s)", "Account message")
    customers.values.forEach {
        val c1 = it.email
        val c2 = if (it.instances.size != 1) "instances" else "instance"
        val c3 = it.instances.values.joinToString(", ") { it.name }
        val c4 = if (it.instances.size != 1 || it.instances.values.first().usernames.size != 1) "accounts" else "account"
        val c5 = when {
            it.instances.size == 1 -> it.instances.values.first().usernames.joinToString(", ")
            else -> it.instances.values.map { "at ${it.name}: ${it.usernames.joinToString(", ")}" }.joinToString("; ")
        }

        csvPrinter.printRecord(c1, c2, c3, c4, c5)
    }

    csvPrinter.close()
}

class Customer(val email: String) {
    val instances = LinkedHashMap<String, Instance>()
}

class Instance(val name: String) {
    val usernames = ArrayList<String>()
}