import java.util.List;

public class Main {

    public static void main(String[] args) {

        SQLite db = new SQLite("jdbc:sqlite:test.db");


        int zurId = 1;
        List<ZUR_DET> zurDet = ZUR_DET.getZURDETList(db, zurId);

        // Printing out the records
        for (ZUR_DET record : zurDet) {
            System.out.println("ID: " + record.getId() + ", ZUR_ID: " + record.getZurId() +
                    ", PAV: " + record.getPav() + ", TIP_ID: " + record.getTipId() +
                    ", LEN: " + record.getLen() + ", MIN: " + record.getMin() +
                    ", MAX: " + record.getMax());
        }

        List<DUOM_DET> duomDet = DUOM_DET.getDUOM_DETList(db, zurId);

        for(DUOM_DET record : duomDet) {
            System.out.println("ID: " + record.getId() + ", ZUR_ID: " + record.getZurId() +
                    ", ZUR_DET_ID: " + record.getZurDetId() + ", LINE_ID: " + record.getLineId() +
                    ", DUOM: " + record.getDuom());
        }
    }
}
