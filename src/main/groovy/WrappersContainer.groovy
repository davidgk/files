import java.util.stream.Collectors
import java.util.stream.Stream

class WrappersContainer {
    List<FileWrapper> fileWrappers
    Stream<FileWrapper> others
    List<String> notReadble
    Stream<FileWrapper> leftGuys
    Stream<FileWrapper> rightGuys

    static WrappersContainer create(List<FileWrapper> fileWrappers) {
        WrappersContainer container = new WrappersContainer();
        container.fileWrappers = fileWrappers;
        container
    }

    def separateThem() {
        others = getOthers()
        /**def electables = getElectables()
        notReadble = getCorruptNames()
        leftGuys = electables.filter {!it.isRight}
        rightGuys = electables.filter {it.isRight}*/
        this
    }



    private Stream<FileWrapper> getElectables() {
        fileWrappers.stream().filter { it.isElectable && it.isReadable}
    }

    def orderThem(LinkedHashMap<String, List<String>> map) {
        def leftExistingIntoRight = leftGuys.collect {it.code}.stream().filter(rightGuys.collect{it.code})
        def rightExistingIntoLeft = rightGuys.collect {it.code}.stream().filter(leftGuys.collect{it.code})

        map.put('correct', getCorrect())
        map.put('different', getDifferent())
        map.put('alone', getDifferent())


    }

    def orderThem() {
        LinkedHashMap<String, Reporter> map = [:]
        map.put('others', getOthers())
        map.put('corrupts', getCorruptNames())
        map.put('alone', getAlones())
        map
    }

    protected Reporter getOthers() {
        def result = fileWrappers.stream().filter { !it.isElectable }.collect { it.file.name }
        return [getReport:{ "["+String.join(",",result)+"]"}] as Reporter

    }

    protected Reporter getCorruptNames() {
        def result = fileWrappers.stream().filter { !it.isReadable }.collect { it.file.name }
        return [getReport:{ "["+String.join(",",result)+"]"}] as Reporter
    }

    protected Reporter getAlones() {
        List<FileWrapper> rights = fileWrappers.stream().filter { it.isRight }.collect()
        List<FileWrapper> lefts = fileWrappers.stream().filter { !it.isRight & it.isElectable }.collect()
        def rightsNotExistsOnLefts = rights.stream().filter{!lefts.collect{it.code}.contains(it.code)}.collect{it.code}
        def leftsNotExistsOnRights = lefts.stream().filter{!rights.collect{it.code}.contains(it.code)}.collect{it.code}
        return [getReport:{ "[left:["+String.join(",", leftsNotExistsOnRights) + "], right:["+ String.join(",", rightsNotExistsOnLefts)+"]]"}] as Reporter
    }
}
