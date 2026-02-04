@org.springframework.modulith.ApplicationModule(
        displayName = "Reminder",
        allowedDependencies = { "core", "user" } // reminder может слушать события user
)
package dev.madela.reminder;
