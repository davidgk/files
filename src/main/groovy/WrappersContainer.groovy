class WrappersContainer {
    List<FileWrapper> fileWrappers
    List<FileWrapper> rights
    List<FileWrapper> lefts
    List<FileWrapper> rightsNotExistsOnLefts
    List<FileWrapper> leftsNotExistsOnRights

    static WrappersContainer create(List<FileWrapper> fileWrappers) {
        WrappersContainer container = new WrappersContainer();
        container.fileWrappers = fileWrappers;
        container.separateElectablesLeftFromRights()
        container
    }

    def orderThem() {
        LinkedHashMap<String, Reporter> map = [:]
        map.put('ignored', getIgnored())
        map.put('failed_pairs', getFailedPairs())

        separateElectablesLeftFromRights()
        map.put('orphans', getOrphans())
        map.put('pairs', getPairs())
        map
    }

    def separateElectablesLeftFromRights() {
        rights = fileWrappers.stream().filter { it.isRight && it.isElectable }.collect()
        lefts = fileWrappers.stream().filter { !it.isRight && it.isElectable }.collect()
        rightsNotExistsOnLefts = rights.stream().filter{!lefts.collect{it.code}.contains(it.code)}.collect()
        leftsNotExistsOnRights = lefts.stream().filter{!rights.collect{it.code}.contains(it.code)}.collect()

    }


    protected Reporter getPairs() {
        def content = checkForDifferent(existsInBoth(),{
            wrapper -> ! (lefts.find {it.code.equals(wrapper.code)})
                            .isDifferent(
                        (rights.find {it.code.equals(wrapper.code)})) && wrapper.isReadable})
        def codes = content.collect{"${it.code}-.${it.kind}"}.collect()
        return [getReport:{"[left:["+ prepareFileName( codes, "left")+"],"+
                "right:["+prepareFileName( codes, "right")+"]]" }] as Reporter

        return prepareCommonReport(content)
    }

    protected Reporter getIgnored() {
        def result = fileWrappers.stream().filter { !it.isElectable }.collect { it.file.name }
        return [getReport:{ "["+String.join(",",result)+"]"}] as Reporter

    }

    protected Reporter getFailedPairs() {
        def errorForSizeMisMatch = getErrorForSizeDifferences()
        def errorForCorrupt = getErrorForCannotRead()
        return [getReport:{ "["+errorForSizeMisMatch+","+errorForCorrupt+"]"}] as Reporter
    }

    def getErrorForSizeDifferences() {
        List<FileWrapper> content = checkForDifferent(existsInBoth(),{ wrapper ->lefts.find {it.code.equals(wrapper.code)}
                .isDifferent(
                rights.find {it.code.equals(wrapper.code)})})
        return prepareCommonErrorReport(content, "size mismatch")
    }

    protected String getErrorForCannotRead() {
        def result = fileWrappers.stream().filter { it.isElectable && !it.isReadable }.collect()
        return prepareCommonErrorReport(result, "cannot read")
    }

    protected Reporter getOrphans() {
        def codesForLefts = leftsNotExistsOnRights.collect{"${it.code}-.${it.kind}"}.collect()
        def codesForRight = rightsNotExistsOnLefts.collect{"${it.code}-.${it.kind}"}.collect()
        return [getReport:{"[left:["+ prepareFileName( codesForLefts, "left")+"],"+
                "right:["+prepareFileName( codesForRight, "right")+"]]" }] as Reporter
    }

    private String prepareCommonErrorReport(List<FileWrapper> content, message) {
        def codes = content.collect{"${it.code}-.${it.kind}"}
        return "{error:'"+message+"'" +
                ",left:["+ prepareFileName( codes, "left")+"],"+
                "right:["+prepareFileName( codes, "right")+"]}"
    }

    private String prepareFileName( List<String> codes, String fileHandType) {
        return String.join(','
                , codes.collect{
                    return getFileName(it, fileHandType)
                })
    }

    private GString getFileName(String it, String fileHandType) {
        String[] vals = it.split("-")
        return "${fileHandType}_${vals[0]}${vals[1]}"
    }

    private Reporter prepareCommonReport(content) {
        [getReport: { "[" + String.join(",", content) + "]" }] as Reporter
    }

    private List<FileWrapper> existsInBoth() {
        return lefts.stream().filter{!leftsNotExistsOnRights.collect {it.code}.contains(it.code)}.collect()
    }

    private List<FileWrapper> checkForDifferent(List<FileWrapper> existInBoth, condition) {
        return  existInBoth
                    .stream()
                    .filter{ condition(it)}
                    .collect()
    }


}
