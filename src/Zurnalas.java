public class Zurnalas {

    private String ID;
    private String ZUR_ID;
    private String ZUR_DET_ID;
    private String Line_ID;
    private String DUOM;

    private Zurnalas zurnalai[];

    public Zurnalas(String ID, String ZUR_ID, String ZUR_DET_ID,String Line_ID, String DUOM) {
        this.ID = ID;
        this.ZUR_ID = ZUR_ID;
        this.ZUR_DET_ID = ZUR_DET_ID;
        this.Line_ID = Line_ID;
        this.DUOM = DUOM;

    }


    public String getID() {
        return ID;
    }

    public String getZUR_ID() {
        return ZUR_ID;
    }

    public String getZUR_DET_ID() {
        return ZUR_DET_ID;
    }

    public String getDUOM() {
        return DUOM;
    }

    public String getLine_ID() {
        return Line_ID;
    }


    public Zurnalas[] getZurnalai()
    {
        return zurnalai;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setZUR_ID(String ZUR_ID) {
        this.ZUR_ID = ZUR_ID;
    }

    public void setZUR_DET_ID(String ZUR_DET_ID) {
        this.ZUR_DET_ID = ZUR_DET_ID;
    }

    public void setDUOM(String DUOM) {
        this.DUOM = DUOM;
    }

    public String setLine_ID(String Line_ID) {
        return this.Line_ID = Line_ID;
    }


    @Override
    public String toString() {
        return "Zurnalas{" +
                "pavadinimas='" + ID + '\'' +
                ", kokybe='" + ZUR_ID + '\'' +
                ", kaina='" + ZUR_DET_ID + '\'' +
                ", kiekis='" + DUOM + '\'' +

                '}';
    }
}
