<br/>
<p align="center">
  <h3 align="center">DBI</h3>

  <p align="center">
    Project assignment for Databases (DBI) course
    <br/>
    Universidad del País Vasco / Euskal Herriko Unibertsitatea
    <br/>
    <br/>
    <a href="https://github.com/Bartolumiu/2324_DBI/issues">Report Bug</a>
    ·
    <a href="https://github.com/Bartolumiu/2324_DBI/issues">Request Feature</a>
  </p>
</p>

![Contributors](https://img.shields.io/github/contributors/Bartolumiu/2324_DBI?color=dark-green) ![Issues](https://img.shields.io/github/issues/Bartolumiu/2324_DBI) ![License](https://img.shields.io/github/license/Bartolumiu/2324_DBI) [![Latest](https://img.shields.io/github/v/release/Bartolumiu/2324_DBI)](RELEASE)

## Table Of Contents

* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Manual Compilation](#manual-compilation)
  * [Alternative](#alternative)
* [Contributing](#contributing)
* [License](#license)
* [Authors](#authors)

## Getting Started

To get a local copy of the project up and running follow these steps.

### Prerequisites

* Java Runtime Environment (JRE) or Java Development Kit (JDK)

  * [Java Downloads for All Operating Systems (JRE)](https://www.java.com/download/manual.jsp)
  * [Java Downloads | Oracle (JDK)](https://www.oracle.com/java/technologies/downloads/)
  * [MySQL Connector for Java | MySQL :: Download Connector/J](https://dev.mysql.com/downloads/connector/j/)

### Manual Compilation
`TODO: Fix compilation instructions after the project is finished.`

1. Clone the repo

```sh
git clone https://github.com/Bartolumiu/2324_DBI.git
```

2. Compile the Java classes

```sh
javac -d ./2324_DBI/bin ./2324_DBI/src/*.java
```

3. Build the JAR file
```sh
jar cfe ./2324_DBI/DBI.jar 2324_DBI.MainDBI -C ./2324_DBI/bin .
```

4. Execute the JAR file

```sh
java -jar ./2324_DBI/DBI.jar
```

### Alternative

1. Download the latest release from the [Releases page](https://github.com/Bartolumiu/2324_DBI/releases/latest).

2. Execute the JAR file

```sh
java -jar ./DBI.jar
```

## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.
* If you have suggestions for adding or removing projects, feel free to [open an issue](https://github.com/Bartolumiu/2324_DBI/issues/new) to discuss it, or directly create a pull request after you edit the *README.md* file with necessary changes.
* Please make sure you check your spelling and grammar.
* Create individual PR for each suggestion.

### Creating A Pull Request

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the GNU General Public License v3.0 (or later). See [LICENSE](https://github.com/Bartolumiu/2324_DBI/blob/main/LICENSE) for more information.

## Authors

* [Bartolumiu](https://github.com/Bartolumiu/)
