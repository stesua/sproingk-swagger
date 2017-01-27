package hm.binkley.labs

import hm.binkley.labs.State.COMPLETE
import hm.binkley.labs.State.PENDING
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.http.ResponseEntity.status
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.*
import java.net.URI

@RestController
open class GreetingController(private val repository: GreetingRepository) {
    @RequestMapping("/greetings", method = arrayOf(POST))
    fun beginGreeting(
            @RequestBody greeting: BeginGreeting): ResponseEntity<*> {
        val name = greeting.name
        repository.create(name)
        return if (null != repository[name])
            status(SEE_OTHER).
                    location(URI.create("/greetings/$name")).
                    body(Status(name, COMPLETE))
        else status(ACCEPTED).
                location(URI.create("/queue/$name")).
                body(Status(name, PENDING))
    }

    @RequestMapping("/queue/{name}", method = arrayOf(GET))
    fun queue(@PathVariable name: String) = try {
        if (null != repository[name])
            status(SEE_OTHER).
                    location(URI.create("/greetings/$name")).
                    body(Status(name, COMPLETE))
        else status(OK).
                body(Status(name, PENDING))
    } catch (_: IndexOutOfBoundsException) {
        status(NOT_FOUND).
                build<Status>()
    }!!

    @RequestMapping("/greetings/{name}", method = arrayOf(GET))
    fun greetings(@PathVariable name: String) = try {
        val greeting = repository[name]
        if (null != greeting)
            ok(Greeting(greeting, Status(name, COMPLETE)))
        else
            status(NOT_FOUND).
                    build()
    } catch (_: IndexOutOfBoundsException) {
        status(NOT_FOUND).
                build<Status>()
    }!!

    @RequestMapping("/queue/{name}", "/greetings/{name}",
            method = arrayOf(DELETE))
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable name: String) = repository.delete(name)
}
