package rxwriter.drug;

import org.junit.jupiter.api.*;
import rxwriter.drug.database.DrugRecord;
import rxwriter.drug.database.DrugSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrugServiceTest implements DrugSource {

    private DrugService drugService;

    @BeforeEach
    void setup() {
       drugService = new DrugService(this);
    }

    @Test
    void drugsAreReturnedSorted() {
        List<DispensableDrug> foundDrugs = drugService.findDrugsStartingWith("as");
        assertNotNull(foundDrugs);
        assertEquals(2, foundDrugs.size());
        assertEquals("asmanex", foundDrugs.get(0).drugName());
        assertEquals("aspirin", foundDrugs.get(1).drugName());
    }

    @Test
    void throwsExceptionOnEmptyStartsWith() {
        Exception thrown = assertThrows(IllegalArgumentException.class,
                ()-> drugService.findDrugsStartingWith("  "));
        System.out.println(thrown.getMessage());
    }

    @Test
    void setsDrugPropertiesCorrectly() {
        List<DispensableDrug> foundDrugs = drugService.findDrugsStartingWith("aspirin");
        DrugClassification[] expectedClassifications = new DrugClassification[] {
                DrugClassification.ANALGESIC, DrugClassification.PLATELET_AGGREGATION_INHIBITORS
        };
        DispensableDrug drug = foundDrugs.get(0);
        assertAll("DispensableDrug properties",
                () -> assertEquals("aspirin", drug.drugName()),
                () -> assertFalse(drug.isControlled()),
                () -> assertEquals(2, drug.drugClassifications().length),
                () -> assertArrayEquals(expectedClassifications, drug.drugClassifications())
        );
    }

    @Override
    public List<DrugRecord> findDrugsStartingWith(String startingString) {
        List<DrugRecord> records = new ArrayList<>();
        if (startingString.equals("as")) {
            records.add(new DrugRecord("asmanex", new int[] {301}, 0));
            records.add(new DrugRecord("aspirin", new int[] {3645, 3530}, 0));
        }
        else if (startingString.equals("aspirin")) {
            records.add(new DrugRecord("aspirin", new int[] {3645, 3530}, 0));
        }
        return records;
    }
}