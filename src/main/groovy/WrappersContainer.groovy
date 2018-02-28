import model.FileWrapper
import model.ValidFileWrapper
import reporter.DefaultReporter
import reporter.FailReporter
import reporter.CommonLeftAndRightReporter
import reporter.Reporter

class WrappersContainer {
    Map<String, List<FileWrapper>> fileWrappers
    List<ValidFileWrapper> rights
    List<ValidFileWrapper> lefts
    List<ValidFileWrapper> rightsNotExistsOnLefts
    List<ValidFileWrapper> leftsNotExistsOnRights

    static WrappersContainer create(Map<String, List<FileWrapper>> filesWrappers) {
        WrappersContainer container = new WrappersContainer();
        container.fileWrappers = filesWrappers;
        container.separateElectablesLeftFromRights()
        container
    }

    private void separateElectablesLeftFromRights() {
        List<ValidFileWrapper> valids = fileWrappers.get("valid");
        rights = valids.stream().filter {it.isRight}.collect()
        lefts =  valids.stream().filter { !it.isRight}.collect()
        rightsNotExistsOnLefts = rights.stream().filter{!lefts.collect{it.code}.contains(it.code)}.collect()
        leftsNotExistsOnRights = lefts.stream().filter{!rights.collect{it.code}.contains(it.code)}.collect()
    }

    def orderThem() {
        LinkedHashMap<String, Reporter> map = [:]
        map.put('ignored', getIgnored())
        map.put('failed_pairs', getFailedPairs())
        map.put('orphans', getOrphans())
        map.put('pairs', getPairs())
        map
    }

    protected Reporter getIgnored() {
        def result = fileWrappers.get("invalid").collect { it.file.name }
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

    protected List<FileWrapper> getErrorForSizeDifferences() {
        return checkForDifferent(existsInBoth(),{ wrapper ->lefts.find {it.code.equals(wrapper.code)}
                .isDifferent(
                rights.find {it.code.equals(wrapper.code)})})
    }

    protected List<FileWrapper> getErrorForCannotRead() {
        List<ValidFileWrapper> valids = fileWrappers.get("valid")
        return  valids.stream().filter { !it.isReadable }.collect()
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
