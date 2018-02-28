package model

import spock.lang.Specification

class FileLoaderTest extends Specification {

    FileSpecification specification = new FileSpecification("./src/test/resources", new FileNameSpecification())

    def "LoadFiles"() {
        given:
            FileLoader loader = new FileLoader(specification)
        when:
            def files  = loader.loadFiles()
        then:
            files.get("valid").size() == 10
            files.get("invalid").size() == 6
    }

    def "GetFilesFromList for a bunch"() {
        given:
            FileLoader loader = new FileLoader(specification)
        when:
            List<File> filesFromList =  loader.getFilesFromList()
        then:
            filesFromList.contains(new File("./src/test/resources/left_001.png"))
            filesFromList.contains(new File("./src/test/resources/left_002.png"))
            filesFromList.contains(new File("./src/test/resources/left_003.png"))
            filesFromList.contains(new File("./src/test/resources/left_005.png"))
            filesFromList.contains(new File("./src/test/resources/left_006.png"))
            filesFromList.contains(new File("./src/test/resources/right_001.png"))
            filesFromList.contains(new File("./src/test/resources/right_002.png"))
            filesFromList.contains(new File("./src/test/resources/right_004.png"))
            filesFromList.contains(new File("./src/test/resources/right_005.png"))
            filesFromList.contains(new File("./src/test/resources/right_006.png"))
            filesFromList.contains(new File("./src/test/resources/foo.txt"))
    }

    def "GetFilesFromList for a single"() {
        given:
            specification.path = "./src/test/resources/left_001.png"
            FileLoader loader = new FileLoader(specification)
        when:
            List<File> filesFromList =  loader.getFilesFromList()
        then:
            filesFromList.contains(new File("./src/test/resources/left_001.png"))
    }

    def "load files with one valid png"() {
        given:
            specification.path = "./src/test/resources/left_001.png"
            FileLoader loader = new FileLoader(specification)
        when:
            def files  = loader.loadFiles()
        then:
            files.get("valid").size() == 1
            files.get("valid").get(0).file.name == "left_001.png"
    }

    def "load files with one valid jpg"() {
        given:
            specification.path = "./src/test/resources/left_001.jpg"
            specification.nameSpecification.allowFormats =["jpg","png"]
            FileLoader loader = new FileLoader(specification)
        when:
            def files  = loader.loadFiles()
        then:
            files.get("valid").size() == 1
            files.get("valid").get(0).file.name == "left_001.jpg"
    }

    def "load files with another Criteria"() {
        given:
            specification.path = "./src/test/resources/0001-izquierdo.jpg"
            specification.nameSpecification= new FileNameSpecification(["\\d+"],"-",["izquierdo", "derecho"],["jpg"])
        FileLoader loader = new FileLoader(specification)
        when:
        def files  = loader.loadFiles()
        then:
        files.get("valid").size() == 1
        files.get("valid").get(0).file.name == "0001-izquierdo.jpg"
    }

    def "Create for one file that is electable with name like 001_left.txt"() {
        given:
            def filename = "001_left.txt";
            def path = "./src/test/resources/${filename}"
        expect:
            executeThis(path, filename)
    }

    def "Create for one file that is electable with name like left_001.txt 01"() {
        given:
        def filename = "left_001.txt";
        def path = "./src/test/resources/${filename}"
        expect:
        executeThis(path, filename)
    }
    def "Create for one file that is electable with name like left001.png 02"() {
        given:
        def filename = "left001.png";
        def path = "./src/test/resources/${filename}"
        expect:
        executeThis(path, filename)
    }

    def "Create for one file that is electable with name like ASDleft_001.png 03"() {
        given:
        def filename = "ASDleft_001.png"
        def path = "./src/test/resources/${filename}"
        expect:
        executeThis(path, filename)
    }

    def executeThis(String path, filename) {
        specification.path =path
        def files  = new FileLoader(specification).loadFiles()
        files.get("valid") == null &&
        files.get("invalid").size() == 1 &&
        files.get("invalid").get(0).file.name == filename
    }


}
