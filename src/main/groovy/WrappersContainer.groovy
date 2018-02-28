import model.FileWrapper
import reporter.DefaultReporter
import reporter.FailReporter
import reporter.CommonLeftAndRightReporter
import reporter.Reporter

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

    protected Reporter getIgnored() {
        def result = fileWrappers.stream().filter { !it.isElectable }.collect { it.file.name }
        return DefaultReporter.create(result)

    }

    protected Reporter getOrphans() {
        return CommonLeftAndRightReporter.create(leftsNotExistsOnRights, rightsNotExistsOnLefts)
    }

    protected Reporter getFailedPairs() {
        def errorForSizeMisMatch = getErrorForSizeDifferences()
        def errorForCorrupt = getErrorForCannotRead()
        def reportMismatch = FailReporter.create("size mismatch", errorForSizeMisMatch).report
        def reportCannotRead = FailReporter.create("cannot read", errorForCorrupt).report
        return [getReport:{"["+ reportMismatch+","+ reportCannotRead+"]"}] as Reporter
    }

    protected Reporter getPairs() {
        def different = checkForDifferent(existsInBoth(), {
            wrapper ->
                !(lefts.find { it.code.equals(wrapper.code) })
                        .isDifferent(
                        (rights.find { it.code.equals(wrapper.code) })) && wrapper.isReadable
        })
        return CommonLeftAndRightReporter.createFromOnlyOneList(different)
    }

    def getErrorForSizeDifferences() {
        return checkForDifferent(existsInBoth(),{ wrapper ->lefts.find {it.code.equals(wrapper.code)}
                .isDifferent(
                rights.find {it.code.equals(wrapper.code)})})
    }

    protected List<FileWrapper> getErrorForCannotRead() {
        return  fileWrappers.stream().filter { it.isElectable && !it.isReadable }.collect()
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
