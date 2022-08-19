# Kscript Tools

Easy way to run shell command line in kotlin and other tools

## Usage

### Used in [kscript](https://github.com/holgerbrandl/kscript):

```kotlin
@file:DependsOn("com.sealwu:kscript-tools:1.0.1")
```

### Used in normal kotlin gradle projects

add into gradle dependencies

```kotlin
dependencies {
    implementation("com.sealwu:kscript-tools:1.0.1")
}
```

## APIs

### `runCommand`
Run a bash/shell command in kotlin code, also can run multi-lines bash/shell scripts

execute next kotlin code
```kotlin
"ls".runCommand()
```
output on console:
```shell
Applications   Downloads      MavenDep       Pictures       iOSProjects
Desktop        IdeaProjects   Movies         Public         scripts
Documents      Library        Music          StudioProjects
```

### `evalBash`
Run a bash/shell command in kotlin code, also can run multi-lines bash/shell scripts,
The different with `runCommand` is that it can get the command run standard output and error output

execute next kotlin code
```kotlin
val date = "date".evalBash().getOrThrow()  //execute shell command `date` and get the command's output and set the content to date variable
println(date) //This will print Fri Aug 19 21:59:56 CEST 2022 on console
val year = date.substringAfterLast(" ") // will get 2022 and assign to `year`
println(year)
```

output on console:
```shell
Fri Aug 19 21:59:56 CEST 2022
2022
```
