# NASA OpenAPI Service

A Spring Boot service designed to interact with NASA's OpenAPIs and manage Web Push notifications. This project is configured to handle sensitive VAPID keys securely across local and production environments.

---

## 🛠 Prerequisites

* **Java 17** (or higher)
* **Gradle**
* **NASA API Key** (Get one at [api.nasa.gov](https://api.nasa.gov/))

---

## 🏃 Local Development Setup

[asdf](https://asdf-vm.com/) can be used for setting up the JDK for dev.

If java plugin is not installed run, Else skip to the next

```bash
asdf plugin-add java
```
Install java version

```bash
asdf install
```

### 1. Setup Configuration
Copy the template file to create your personal local configuration.

```bash
# Navigate to the resources folder
cd nasa-service/src/main/resources

# Create your local config from the example template
cp application-local.yml.example application-local.yml
```


### 2. Update keys in local configuration
Update the VAPID Keys and NASA API token in local yml file

### 3. Run the application
```bash

# Return to project root
cd ../../../

# Run with the 'local' profile active
./gradlew bootRun --args='--spring.profiles.active=local'
```