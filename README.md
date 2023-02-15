# Integration tests for Real World app

## About

This is a test automation framework with a list of the tests
for [Real World example apps](https://github.com/gothinkster/realworld).

## Requirements

| Plugin | README |
| ------ | ------ |
| Java (at least 11) | https://www.oracle.com/java/technologies/javase-downloads.html |
| Maven (at least 3) | https://maven.apache.org/download.cgi |
| Google Chrome (latest) | https://www.google.com/chrome/ |

## Execution

To trigger the tests on local environment use follow command:

```sh
mvn test
```

This command will download [chromedriver](https://chromedriver.chromium.org) and trigger your tests against Google
Chrome

You can also trigger the test against [Selenium Grid](https://www.selenium.dev/documentation/en/grid/) using following
command:

```sh
mvn test -Dgrid.url=[url]
```

where:

- `[url]` - link to your Selenium Grid instance, example: http://localhost:4444/wd/hub

## License

MIT