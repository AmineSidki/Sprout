package com.example.mock.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mockentity101_table")
public class MockEntity101 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "field_1_5365")
    private Boolean field15365;
    @Column(name = "field_2_1062")
    private LocalDateTime field21062;
    @Column(name = "field_3_7847")
    private LocalDateTime field37847;
    @Column(name = "field_4_3894")
    private UUID field43894;
    @Column(name = "field_5_6569")
    private UUID field56569;
    @Column(name = "field_6_3914")
    private String field63914;
    @Column(name = "field_7_8590")
    private Double field78590;
    @Column(name = "field_8_1857")
    private String field81857;
    @Column(name = "field_9_2910")
    private Long field92910;
    @Column(name = "field_10_3483")
    private Double field103483;
    @Column(name = "field_11_6741")
    private Boolean field116741;
    @Column(name = "field_12_4361")
    private Long field124361;
    @Column(name = "field_13_2599")
    private UUID field132599;
    @Column(name = "field_14_6783")
    private Double field146783;
    @Column(name = "field_15_2799")
    private BigDecimal field152799;
    @Column(name = "field_16_7064")
    private Integer field167064;
    @Column(name = "field_17_5626")
    private Double field175626;
    @Column(name = "field_18_2604")
    private Long field182604;
    @Column(name = "field_19_8083")
    private UUID field198083;
    @Column(name = "field_20_8469")
    private LocalDate field208469;
    @Column(name = "field_21_5072")
    private LocalDate field215072;
    @Column(name = "field_22_9119")
    private UUID field229119;
    @Column(name = "field_23_3661")
    private LocalDate field233661;
    @Column(name = "field_24_3478")
    private Integer field243478;
    @Column(name = "field_25_5710")
    private BigDecimal field255710;
    @Column(name = "field_26_1728")
    private BigDecimal field261728;
    @Column(name = "field_27_2020")
    private UUID field272020;
    @Column(name = "field_28_6226")
    private String field286226;
    @Column(name = "field_29_4196")
    private LocalDateTime field294196;
    @Column(name = "field_30_1534")
    private Double field301534;
    @Column(name = "field_31_1157")
    private Long field311157;
    @Column(name = "field_32_5079")
    private LocalDateTime field325079;
    @Column(name = "field_33_6885")
    private String field336885;
    @Column(name = "field_34_3964")
    private Boolean field343964;
    @Column(name = "field_35_9701")
    private Integer field359701;
    @Column(name = "field_36_9400")
    private Integer field369400;
    @Column(name = "field_37_1912")
    private Boolean field371912;
    @Column(name = "field_38_8468")
    private Integer field388468;
    @Column(name = "field_39_3435")
    private Long field393435;
    @Column(name = "field_40_5860")
    private Long field405860;
    @Column(name = "field_41_3492")
    private LocalDateTime field413492;
    @Column(name = "field_42_7236")
    private Double field427236;
    @Column(name = "field_43_7718")
    private String field437718;
    @Column(name = "field_44_6895")
    private Boolean field446895;
    @Column(name = "field_45_8705")
    private String field458705;
    @Column(name = "field_46_2747")
    private BigDecimal field462747;
    @Column(name = "field_47_8667")
    private LocalDate field478667;
    @Column(name = "field_48_9060")
    private Double field489060;
    @Column(name = "field_49_2610")
    private UUID field492610;
    @Column(name = "field_50_2966")
    private LocalDate field502966;
    @Column(name = "field_51_4866")
    private Double field514866;
    @Column(name = "field_52_3030")
    private UUID field523030;
    @Column(name = "field_53_9633")
    private Integer field539633;
    @Column(name = "field_54_5832")
    private LocalDate field545832;
    @Column(name = "field_55_5232")
    private LocalDate field555232;
    @Column(name = "field_56_7197")
    private Integer field567197;
    @Column(name = "field_57_9869")
    private String field579869;
    @Column(name = "field_58_4521")
    private BigDecimal field584521;
    @Column(name = "field_59_8821")
    private LocalDateTime field598821;
    @Column(name = "field_60_5083")
    private String field605083;
    @Column(name = "field_61_6763")
    private BigDecimal field616763;
    @Column(name = "field_62_8649")
    private Integer field628649;
    @Column(name = "field_63_7001")
    private Integer field637001;
    @Column(name = "field_64_1885")
    private Boolean field641885;
    @Column(name = "field_65_4879")
    private BigDecimal field654879;
    @Column(name = "field_66_4455")
    private Double field664455;
    @Column(name = "field_67_3188")
    private UUID field673188;
    @Column(name = "field_68_4563")
    private LocalDateTime field684563;
    @Column(name = "field_69_5393")
    private Boolean field695393;
    @Column(name = "field_70_6489")
    private Double field706489;
    @Column(name = "field_71_5230")
    private Long field715230;
    @Column(name = "field_72_3815")
    private Long field723815;
    @Column(name = "field_73_5243")
    private Double field735243;
    @Column(name = "field_74_1268")
    private LocalDate field741268;
    @Column(name = "field_75_7120")
    private LocalDate field757120;
    @Column(name = "field_76_2996")
    private String field762996;
    @Column(name = "field_77_9821")
    private Boolean field779821;
    @Column(name = "field_78_8211")
    private UUID field788211;
    @Column(name = "field_79_7813")
    private LocalDate field797813;
    @Column(name = "field_80_6710")
    private LocalDate field806710;
    @Column(name = "field_81_4514")
    private Boolean field814514;
    @Column(name = "field_82_6405")
    private Long field826405;
    @Column(name = "field_83_7540")
    private LocalDate field837540;
    @Column(name = "field_84_5707")
    private Double field845707;
    @Column(name = "field_85_4883")
    private Boolean field854883;
    @Column(name = "field_86_6871")
    private LocalDate field866871;
    @Column(name = "field_87_6051")
    private Long field876051;
    @Column(name = "field_88_8622")
    private Boolean field888622;
    @Column(name = "field_89_9296")
    private String field899296;
    @Column(name = "field_90_4137")
    private Boolean field904137;
    @Column(name = "field_91_3212")
    private String field913212;
    @Column(name = "field_92_3472")
    private Boolean field923472;
    @Column(name = "field_93_9246")
    private BigDecimal field939246;
    @Column(name = "field_94_5405")
    private Long field945405;
    @Column(name = "field_95_3587")
    private Double field953587;
    @Column(name = "field_96_1262")
    private String field961262;
    @Column(name = "field_97_6436")
    private LocalDateTime field976436;
    @Column(name = "field_98_6059")
    private Long field986059;
    @Column(name = "field_99_2732")
    private String field992732;
    @Column(name = "field_100_7652")
    private LocalDateTime field1007652;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getField15365() {
        return field15365;
    }

    public void setField15365(Boolean field15365) {
        this.field15365 = field15365;
    }

    public LocalDateTime getField21062() {
        return field21062;
    }

    public void setField21062(LocalDateTime field21062) {
        this.field21062 = field21062;
    }

    public LocalDateTime getField37847() {
        return field37847;
    }

    public void setField37847(LocalDateTime field37847) {
        this.field37847 = field37847;
    }

    public UUID getField43894() {
        return field43894;
    }

    public void setField43894(UUID field43894) {
        this.field43894 = field43894;
    }

    public UUID getField56569() {
        return field56569;
    }

    public void setField56569(UUID field56569) {
        this.field56569 = field56569;
    }

    public String getField63914() {
        return field63914;
    }

    public void setField63914(String field63914) {
        this.field63914 = field63914;
    }

    public Double getField78590() {
        return field78590;
    }

    public void setField78590(Double field78590) {
        this.field78590 = field78590;
    }

    public String getField81857() {
        return field81857;
    }

    public void setField81857(String field81857) {
        this.field81857 = field81857;
    }

    public Long getField92910() {
        return field92910;
    }

    public void setField92910(Long field92910) {
        this.field92910 = field92910;
    }

    public Double getField103483() {
        return field103483;
    }

    public void setField103483(Double field103483) {
        this.field103483 = field103483;
    }

    public Boolean getField116741() {
        return field116741;
    }

    public void setField116741(Boolean field116741) {
        this.field116741 = field116741;
    }

    public Long getField124361() {
        return field124361;
    }

    public void setField124361(Long field124361) {
        this.field124361 = field124361;
    }

    public UUID getField132599() {
        return field132599;
    }

    public void setField132599(UUID field132599) {
        this.field132599 = field132599;
    }

    public Double getField146783() {
        return field146783;
    }

    public void setField146783(Double field146783) {
        this.field146783 = field146783;
    }

    public BigDecimal getField152799() {
        return field152799;
    }

    public void setField152799(BigDecimal field152799) {
        this.field152799 = field152799;
    }

    public Integer getField167064() {
        return field167064;
    }

    public void setField167064(Integer field167064) {
        this.field167064 = field167064;
    }

    public Double getField175626() {
        return field175626;
    }

    public void setField175626(Double field175626) {
        this.field175626 = field175626;
    }

    public Long getField182604() {
        return field182604;
    }

    public void setField182604(Long field182604) {
        this.field182604 = field182604;
    }

    public UUID getField198083() {
        return field198083;
    }

    public void setField198083(UUID field198083) {
        this.field198083 = field198083;
    }

    public LocalDate getField208469() {
        return field208469;
    }

    public void setField208469(LocalDate field208469) {
        this.field208469 = field208469;
    }

    public LocalDate getField215072() {
        return field215072;
    }

    public void setField215072(LocalDate field215072) {
        this.field215072 = field215072;
    }

    public UUID getField229119() {
        return field229119;
    }

    public void setField229119(UUID field229119) {
        this.field229119 = field229119;
    }

    public LocalDate getField233661() {
        return field233661;
    }

    public void setField233661(LocalDate field233661) {
        this.field233661 = field233661;
    }

    public Integer getField243478() {
        return field243478;
    }

    public void setField243478(Integer field243478) {
        this.field243478 = field243478;
    }

    public BigDecimal getField255710() {
        return field255710;
    }

    public void setField255710(BigDecimal field255710) {
        this.field255710 = field255710;
    }

    public BigDecimal getField261728() {
        return field261728;
    }

    public void setField261728(BigDecimal field261728) {
        this.field261728 = field261728;
    }

    public UUID getField272020() {
        return field272020;
    }

    public void setField272020(UUID field272020) {
        this.field272020 = field272020;
    }

    public String getField286226() {
        return field286226;
    }

    public void setField286226(String field286226) {
        this.field286226 = field286226;
    }

    public LocalDateTime getField294196() {
        return field294196;
    }

    public void setField294196(LocalDateTime field294196) {
        this.field294196 = field294196;
    }

    public Double getField301534() {
        return field301534;
    }

    public void setField301534(Double field301534) {
        this.field301534 = field301534;
    }

    public Long getField311157() {
        return field311157;
    }

    public void setField311157(Long field311157) {
        this.field311157 = field311157;
    }

    public LocalDateTime getField325079() {
        return field325079;
    }

    public void setField325079(LocalDateTime field325079) {
        this.field325079 = field325079;
    }

    public String getField336885() {
        return field336885;
    }

    public void setField336885(String field336885) {
        this.field336885 = field336885;
    }

    public Boolean getField343964() {
        return field343964;
    }

    public void setField343964(Boolean field343964) {
        this.field343964 = field343964;
    }

    public Integer getField359701() {
        return field359701;
    }

    public void setField359701(Integer field359701) {
        this.field359701 = field359701;
    }

    public Integer getField369400() {
        return field369400;
    }

    public void setField369400(Integer field369400) {
        this.field369400 = field369400;
    }

    public Boolean getField371912() {
        return field371912;
    }

    public void setField371912(Boolean field371912) {
        this.field371912 = field371912;
    }

    public Integer getField388468() {
        return field388468;
    }

    public void setField388468(Integer field388468) {
        this.field388468 = field388468;
    }

    public Long getField393435() {
        return field393435;
    }

    public void setField393435(Long field393435) {
        this.field393435 = field393435;
    }

    public Long getField405860() {
        return field405860;
    }

    public void setField405860(Long field405860) {
        this.field405860 = field405860;
    }

    public LocalDateTime getField413492() {
        return field413492;
    }

    public void setField413492(LocalDateTime field413492) {
        this.field413492 = field413492;
    }

    public Double getField427236() {
        return field427236;
    }

    public void setField427236(Double field427236) {
        this.field427236 = field427236;
    }

    public String getField437718() {
        return field437718;
    }

    public void setField437718(String field437718) {
        this.field437718 = field437718;
    }

    public Boolean getField446895() {
        return field446895;
    }

    public void setField446895(Boolean field446895) {
        this.field446895 = field446895;
    }

    public String getField458705() {
        return field458705;
    }

    public void setField458705(String field458705) {
        this.field458705 = field458705;
    }

    public BigDecimal getField462747() {
        return field462747;
    }

    public void setField462747(BigDecimal field462747) {
        this.field462747 = field462747;
    }

    public LocalDate getField478667() {
        return field478667;
    }

    public void setField478667(LocalDate field478667) {
        this.field478667 = field478667;
    }

    public Double getField489060() {
        return field489060;
    }

    public void setField489060(Double field489060) {
        this.field489060 = field489060;
    }

    public UUID getField492610() {
        return field492610;
    }

    public void setField492610(UUID field492610) {
        this.field492610 = field492610;
    }

    public LocalDate getField502966() {
        return field502966;
    }

    public void setField502966(LocalDate field502966) {
        this.field502966 = field502966;
    }

    public Double getField514866() {
        return field514866;
    }

    public void setField514866(Double field514866) {
        this.field514866 = field514866;
    }

    public UUID getField523030() {
        return field523030;
    }

    public void setField523030(UUID field523030) {
        this.field523030 = field523030;
    }

    public Integer getField539633() {
        return field539633;
    }

    public void setField539633(Integer field539633) {
        this.field539633 = field539633;
    }

    public LocalDate getField545832() {
        return field545832;
    }

    public void setField545832(LocalDate field545832) {
        this.field545832 = field545832;
    }

    public LocalDate getField555232() {
        return field555232;
    }

    public void setField555232(LocalDate field555232) {
        this.field555232 = field555232;
    }

    public Integer getField567197() {
        return field567197;
    }

    public void setField567197(Integer field567197) {
        this.field567197 = field567197;
    }

    public String getField579869() {
        return field579869;
    }

    public void setField579869(String field579869) {
        this.field579869 = field579869;
    }

    public BigDecimal getField584521() {
        return field584521;
    }

    public void setField584521(BigDecimal field584521) {
        this.field584521 = field584521;
    }

    public LocalDateTime getField598821() {
        return field598821;
    }

    public void setField598821(LocalDateTime field598821) {
        this.field598821 = field598821;
    }

    public String getField605083() {
        return field605083;
    }

    public void setField605083(String field605083) {
        this.field605083 = field605083;
    }

    public BigDecimal getField616763() {
        return field616763;
    }

    public void setField616763(BigDecimal field616763) {
        this.field616763 = field616763;
    }

    public Integer getField628649() {
        return field628649;
    }

    public void setField628649(Integer field628649) {
        this.field628649 = field628649;
    }

    public Integer getField637001() {
        return field637001;
    }

    public void setField637001(Integer field637001) {
        this.field637001 = field637001;
    }

    public Boolean getField641885() {
        return field641885;
    }

    public void setField641885(Boolean field641885) {
        this.field641885 = field641885;
    }

    public BigDecimal getField654879() {
        return field654879;
    }

    public void setField654879(BigDecimal field654879) {
        this.field654879 = field654879;
    }

    public Double getField664455() {
        return field664455;
    }

    public void setField664455(Double field664455) {
        this.field664455 = field664455;
    }

    public UUID getField673188() {
        return field673188;
    }

    public void setField673188(UUID field673188) {
        this.field673188 = field673188;
    }

    public LocalDateTime getField684563() {
        return field684563;
    }

    public void setField684563(LocalDateTime field684563) {
        this.field684563 = field684563;
    }

    public Boolean getField695393() {
        return field695393;
    }

    public void setField695393(Boolean field695393) {
        this.field695393 = field695393;
    }

    public Double getField706489() {
        return field706489;
    }

    public void setField706489(Double field706489) {
        this.field706489 = field706489;
    }

    public Long getField715230() {
        return field715230;
    }

    public void setField715230(Long field715230) {
        this.field715230 = field715230;
    }

    public Long getField723815() {
        return field723815;
    }

    public void setField723815(Long field723815) {
        this.field723815 = field723815;
    }

    public Double getField735243() {
        return field735243;
    }

    public void setField735243(Double field735243) {
        this.field735243 = field735243;
    }

    public LocalDate getField741268() {
        return field741268;
    }

    public void setField741268(LocalDate field741268) {
        this.field741268 = field741268;
    }

    public LocalDate getField757120() {
        return field757120;
    }

    public void setField757120(LocalDate field757120) {
        this.field757120 = field757120;
    }

    public String getField762996() {
        return field762996;
    }

    public void setField762996(String field762996) {
        this.field762996 = field762996;
    }

    public Boolean getField779821() {
        return field779821;
    }

    public void setField779821(Boolean field779821) {
        this.field779821 = field779821;
    }

    public UUID getField788211() {
        return field788211;
    }

    public void setField788211(UUID field788211) {
        this.field788211 = field788211;
    }

    public LocalDate getField797813() {
        return field797813;
    }

    public void setField797813(LocalDate field797813) {
        this.field797813 = field797813;
    }

    public LocalDate getField806710() {
        return field806710;
    }

    public void setField806710(LocalDate field806710) {
        this.field806710 = field806710;
    }

    public Boolean getField814514() {
        return field814514;
    }

    public void setField814514(Boolean field814514) {
        this.field814514 = field814514;
    }

    public Long getField826405() {
        return field826405;
    }

    public void setField826405(Long field826405) {
        this.field826405 = field826405;
    }

    public LocalDate getField837540() {
        return field837540;
    }

    public void setField837540(LocalDate field837540) {
        this.field837540 = field837540;
    }

    public Double getField845707() {
        return field845707;
    }

    public void setField845707(Double field845707) {
        this.field845707 = field845707;
    }

    public Boolean getField854883() {
        return field854883;
    }

    public void setField854883(Boolean field854883) {
        this.field854883 = field854883;
    }

    public LocalDate getField866871() {
        return field866871;
    }

    public void setField866871(LocalDate field866871) {
        this.field866871 = field866871;
    }

    public Long getField876051() {
        return field876051;
    }

    public void setField876051(Long field876051) {
        this.field876051 = field876051;
    }

    public Boolean getField888622() {
        return field888622;
    }

    public void setField888622(Boolean field888622) {
        this.field888622 = field888622;
    }

    public String getField899296() {
        return field899296;
    }

    public void setField899296(String field899296) {
        this.field899296 = field899296;
    }

    public Boolean getField904137() {
        return field904137;
    }

    public void setField904137(Boolean field904137) {
        this.field904137 = field904137;
    }

    public String getField913212() {
        return field913212;
    }

    public void setField913212(String field913212) {
        this.field913212 = field913212;
    }

    public Boolean getField923472() {
        return field923472;
    }

    public void setField923472(Boolean field923472) {
        this.field923472 = field923472;
    }

    public BigDecimal getField939246() {
        return field939246;
    }

    public void setField939246(BigDecimal field939246) {
        this.field939246 = field939246;
    }

    public Long getField945405() {
        return field945405;
    }

    public void setField945405(Long field945405) {
        this.field945405 = field945405;
    }

    public Double getField953587() {
        return field953587;
    }

    public void setField953587(Double field953587) {
        this.field953587 = field953587;
    }

    public String getField961262() {
        return field961262;
    }

    public void setField961262(String field961262) {
        this.field961262 = field961262;
    }

    public LocalDateTime getField976436() {
        return field976436;
    }

    public void setField976436(LocalDateTime field976436) {
        this.field976436 = field976436;
    }

    public Long getField986059() {
        return field986059;
    }

    public void setField986059(Long field986059) {
        this.field986059 = field986059;
    }

    public String getField992732() {
        return field992732;
    }

    public void setField992732(String field992732) {
        this.field992732 = field992732;
    }

    public LocalDateTime getField1007652() {
        return field1007652;
    }

    public void setField1007652(LocalDateTime field1007652) {
        this.field1007652 = field1007652;
    }

}