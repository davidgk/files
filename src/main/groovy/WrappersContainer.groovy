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
        map.put('ignored', getIgnored())
        map.put('failed_pairs', getFailedPairs())

        separateElectablesLeftFromRights()
        map.put('alone', getAlones())
        map.put('correct', getCorrects())
        map
    }

    def separateElectablesLeftFromRights() {
        rights = fileWrappers.stream().filter { it.isRight && it.isElectable }.collect()
        lefts = fileWrappers.stream().filter { !it.isRight && it.isElectable }.collect()
        rightsNotExistsOnLefts = rights.stream().filter{!lefts.collect{it.code}.contains(it.code)}.collect{it.code}
        leftsNotExistsOnRights = lefts.stream().filter{!rights.collect{it.code}.contains(it.code)}.collect{it.code}

    }

    protected Reporter getIgnored() {
        def result = fileWrappers.stream().filter { !it.isElectable }.collect { it.file.name }
        return [getReport:{ "["+String.join(",",result)+"]"}] as Reporter

    }

    protected Reporter getFailedPairs() {
        def errorForSizeMisMatch = getErrorForSizeDifferences()
        def errorForCorrupt = getErrorForCannotRead()
        return [getReport:{ "{"+errorForSizeMisMatch+","+errorForCorrupt+"}"}] as Reporter
    }

    def getErrorForSizeDifferences() {
        List<FileWrapper> content = checkAreDifferent(existsInBoth(),{wrapper ->lefts.find {it.code.equals(wrapper.code)}
                .isDifferent(
                rights.find {it.code.equals(wrapper.code)})})
        return prepareCommonErrorReport(content, "size mismatch")
    }

    def getErrorForCannotRead() {
        def result = fileWrappers.stream().filter { !it.isReadable }.collect { it.code }
        return prepareCommonErrorReport(result, "cannot read")
    }

    protected Reporter getAlones() {
        return [getReport:{ "[left:["+String.join(",", leftsNotExistsOnRights) + "], right:["+ String.join(",", rightsNotExistsOnLefts)+"]]"}] as Reporter
    }

    protected Reporter getCorrects() {
        def content = checkAreDifferent(existsInBoth(),{wrapper -> ! lefts.find {it.code.equals(wrapper.code)}
                .isDifferent(
                rights.find {it.code.equals(wrapper.code)})})
        return prepareCommonReport(content)
    }

    private String prepareCommonErrorReport(List<FileWrapper> content, message) {
        def codes = content.collect{"${it.code}-.${it.kind}"}
        return "[error: '"+message+"'" +
                ", left: ["+ prepareFileName( codes, "left")+"],"+
                "right: ["+prepareFileName( codes, "right")+"]]"
    }

    def prepareFileName( List<String> codes, String fileHandType) {
        return String.join(','
                , codes.collect{
                    String[] vals = it.split("-")
                    return "${fileHandType}_${vals[0].$(vals[1])}"
                })
    }

    private Reporter prepareCommonReport(content) {
        [getReport: { "[" + String.join(",", content) + "]" }] as Reporter
    }

    List<FileWrapper> existsInBoth() {
        return lefts.stream().filter{!leftsNotExistsOnRights.contains(it.code)}.collect()
    }

    List<FileWrapper> checkAreDifferent(List<FileWrapper> existInBoth, condition) {
        return  existInBoth
                    .stream()
                    .filter{ condition(it)}
                    .collect()
    }


}
