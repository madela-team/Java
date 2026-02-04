@org.springframework.modulith.ApplicationModule(
        displayName = "Core",
        allowedDependencies = {
                "security",
                "user",
                "reminder",
                "controller"
        }
)
package dev.madela.core;
