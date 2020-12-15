# MyLibreLab

This is a fork of [MyOpenLab](https://myopenlab.org/), which became unmaintained in 2020.
The original sources were gathered from <https://sourceforge.net/p/myopenlab3/code/HEAD/tree/>.

MyLibreLab is currently under heavy development (early alpha stage), and it's not yet functional. If you are looking for a fully functional and stable version, check out original [archived MyOpenLab](https://github.com/MyLibreLab/MyOpenLab).

## Differences to MyOpenLab

- Provides self-contained distribution. No need to install any Java distribution any more.
- Built using modern Java 14 technology
- Maintained on GitHub

## Manual installation:

1. Clone MyLibreLab:

  ```terminal
  git clone https://github.com/MyLibreLab/MyLibreLab.git
  ```

2. Run with gradle:

  ```terminal
  cd MyLibreLab
  ./gradlew run
  ```

## User documentation
[User docs link](https://mylibrelab.github.io/user-documentation/)
## Code Setup

### Java Development Kit 14

Currently, MyLibreLab also works on Java 11.
You can install it using `apt-get install openjdk-11-jdk` on Debian and Ubuntu linux.

A working Java 14 installation is required. In the command line \(terminal in Linux, cmd in Windows\) run `javac -version` and make sure that the reported version is Java 14 \(e.g `javac 14`\). If `javac` is not found or a wrong version is reported, check your PATH environment variable, your `JAVA_HOME` environment variable or install the most recent JDK.

Download the JDK from <https://jdk.java.net/>. On Windows, you can execute `choco install openjdk` (requires [installation of chocolatey - a package manager for Windows](https://chocolatey.org/install)).

### IDE Setup

On Windows:

1. Install NetBeans: `choco install apache-netbeans.portable`
1. Install JDK 14: `choco install libericajdk`
1. Edit `C:\ProgramData\chocolatey\lib\apache-netbeans.portable\App\netbeans\etc\netbeans.conf` (as Admin)

  - `netbeans_jdkhome="C:\Program Files\BellSoft\LibericaJDK-14"`

Then "Apache Netbeans" can be started. Open the folder containing the project.
