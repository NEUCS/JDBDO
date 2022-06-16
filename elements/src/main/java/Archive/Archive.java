package Archive;

import individual.ContinuousIndividual;
import population.Population;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface Archive {
    void loadExternalArchive(String path, Population<ContinuousIndividual> population) throws IOException;
    void outputArchiveToFile(String path) throws IOException;
}
