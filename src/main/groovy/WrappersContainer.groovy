class WrappersContainer {
    List<FileWrapper> fileWrappers
    List<FileWrapper> rights
    List<FileWrapper> lefts
    List<String> rightsNotExistsOnLefts
    List<String> leftsNotExistsOnRights

    static WrappersContainer create(List<FileWrapper> fileWrappers) {
        WrappersContainer container = new WrappersContainer();
        container.fileWrappers = fileWrappers;
        container
    }

    def orderThem() {
        LinkedHashMap<String, Reporter> map = [:]
        map.put('others', getOthers())
        map.put('corrupts', getCorruptNames())

        separateElectablesLeftFromRights()
        map.put('alone', getAlones())
        map.put('different', getDiferents())
        map.put('correct', getCorrects())
        map
    }

    def separateElectablesLeftFromRights() {
        rights = fileWrappers.stream().filter { it.isRight && it.isElectable }.collect()
        lefts = fileWrappers.stream().filter { !it.isRight && it.isElectable }.collect()
        rightsNotExistsOnLefts = rights.stream().filter{!lefts.collect{it.code}.contains(it.code)}.collect{it.code}
        leftsNotExistsOnRights = lefts.stream().filter{!rights.collect{it.code}.contains(it.code)}.collect{it.code}

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
        return [getReport:{ "[left:["+String.join(",", leftsNotExistsOnRights) + "], right:["+ String.join(",", rightsNotExistsOnLefts)+"]]"}] as Reporter
    }

    protected Reporter getDiferents() {
        def content = checkAreDifferent(existsInBoth(),{wrapper ->lefts.find {it.code.equals(wrapper.code)}
                .isDifferent(
                rights.find {it.code.equals(wrapper.code)})})
        return prepareCommonReport(content)
    }

    protected Reporter getCorrects() {
        def content = checkAreDifferent(existsInBoth(),{wrapper -> ! lefts.find {it.code.equals(wrapper.code)}
                .isDifferent(
                rights.find {it.code.equals(wrapper.code)})})
        return prepareCommonReport(content)
    }

    private Reporter prepareCommonReport(content) {
        [getReport: { "[" + String.join(",", content) + "]" }] as Reporter
    }

    List<FileWrapper> existsInBoth() {
        return lefts.stream().filter{!leftsNotExistsOnRights.contains(it.code)}.collect()
    }

    List<String> checkAreDifferent(List<FileWrapper> existInBoth, condition) {
        return  existInBoth
                    .stream()
                    .filter{ condition(it)}
                    .collect{it.code}
    }


}
