package reporter

abstract class Reporter {
    String report


    abstract createReport();


    protected createDelimitedByComas(List<String> values) {
        return String.join(",", values)
    }

}
