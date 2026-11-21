package patmal.workshops.student.impl.logic.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patmal.workshops.api.model.Student;
import patmal.workshops.student.impl.logic.manager.StudentManager;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CsvStudentImporter
{
    private static final Logger logger = LogManager.getLogger(CsvStudentImporter.class);
    private static final String[] HEADER = {"id", "name", "email"};

    private final StudentManager studentManager;

    public CsvStudentImporter(StudentManager studentManager)
    {
        this.studentManager = studentManager;
    }

    public List<Student> importStudents(File source)
    {
        if (source == null)
        {
            throw reject("No file was given to import");
        }

        List<Student> imported = new ArrayList<Student>();
        try (Reader reader = Files.newBufferedReader(source.toPath(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.parse(reader))
        {
            Iterator<CSVRecord> records = parser.iterator();
            if (!records.hasNext())
            {
                throw reject("The file " + source + " is empty - the first line must be the header id,name,email");
            }
            checkHeader(records.next(), source);

            while (records.hasNext())
            {
                CSVRecord record = records.next();
                if (record.size() != HEADER.length)
                {
                    throw reject("Line " + parser.getCurrentLineNumber() + " of " + source + " has " + record.size()
                            + " values instead of " + HEADER.length);
                }
                imported.add(studentManager.createStudent(record.get(0), record.get(1), record.get(2)));
            }
        }
        catch (IOException e)
        {
            logger.warn("Could not read the file {}: {}", source, e.getMessage());
            throw new UncheckedIOException("Could not read the file " + source, e);
        }

        logger.info("Imported {} students from {}", imported.size(), source);
        return imported;
    }

    private void checkHeader(CSVRecord header, File source)
    {
        boolean valid = header.size() == HEADER.length;
        for (int i = 0; valid && i < HEADER.length; i++)
        {
            String value = header.get(i);
            if (i == 0 && value.startsWith("﻿"))
            {
                value = value.substring(1);
            }
            valid = HEADER[i].equals(value);
        }

        if (!valid)
        {
            throw reject("The first line of " + source + " must be the header id,name,email");
        }
    }

    private IllegalArgumentException reject(String message)
    {
        logger.warn("Rejected: {}", message);
        return new IllegalArgumentException(message);
    }
}
