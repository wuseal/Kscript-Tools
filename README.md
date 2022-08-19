# Kscript Tools

Easy way to run shell command line in kotlin and other tools

## Usage

add into gradle dependencies

```kotlin
dependencies {
    implementation("com.sealwu:kscript-tools:1.0.0")
}
```

## APIs

### `runCommand`

execute next kotlin code
```kotlin
"ls".runCommand()
```
output in console:
```shell
Applications   Downloads      MavenDep       Pictures       iOSProjects
Desktop        IdeaProjects   Movies         Public         scripts
Documents      Library        Music          StudioProjects
```

### `evalBash`

execute next kotlin code
```kotlin
val date = "date".evalBash()  //execute command `date` and get the command's output and set the content to date variable
println(date) //This will print Fri Aug 19 21:59:56 CEST 2022 on console
val year = date.substringAfterLast(" ") // will get 2022 and assign to `year`
```

output on console:
```shell
Fri Aug 19 21:59:56 CEST 2022
```
