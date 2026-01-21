package com.example.mock.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mockentity42_table")
public class MockEntity42 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "field_1_9057")
    private Double field19057;
    @Column(name = "field_2_5114")
    private String field25114;
    @Column(name = "field_3_6452")
    private Long field36452;
    @Column(name = "field_4_2750")
    private LocalDateTime field42750;
    @Column(name = "field_5_9872")
    private LocalDateTime field59872;
    @Column(name = "field_6_1938")
    private BigDecimal field61938;
    @Column(name = "field_7_9106")
    private BigDecimal field79106;
    @Column(name = "field_8_8753")
    private String field88753;
    @Column(name = "field_9_7101")
    private Integer field97101;
    @Column(name = "field_10_1384")
    private UUID field101384;
    @Column(name = "field_11_6429")
    private LocalDateTime field116429;
    @Column(name = "field_12_4505")
    private String field124505;
    @Column(name = "field_13_9215")
    private UUID field139215;
    @Column(name = "field_14_7719")
    private LocalDateTime field147719;
    @Column(name = "field_15_3200")
    private BigDecimal field153200;
    @Column(name = "field_16_8060")
    private Integer field168060;
    @Column(name = "field_17_8435")
    private BigDecimal field178435;
    @Column(name = "field_18_2474")
    private BigDecimal field182474;
    @Column(name = "field_19_5205")
    private UUID field195205;
    @Column(name = "field_20_3216")
    private Long field203216;
    @Column(name = "field_21_5842")
    private Boolean field215842;
    @Column(name = "field_22_1701")
    private BigDecimal field221701;
    @Column(name = "field_23_6331")
    private BigDecimal field236331;
    @Column(name = "field_24_7480")
    private UUID field247480;
    @Column(name = "field_25_1425")
    private UUID field251425;
    @Column(name = "field_26_9839")
    private UUID field269839;
    @Column(name = "field_27_4318")
    private Boolean field274318;
    @Column(name = "field_28_5891")
    private Boolean field285891;
    @Column(name = "field_29_4183")
    private Double field294183;
    @Column(name = "field_30_1657")
    private Long field301657;
    @Column(name = "field_31_1701")
    private UUID field311701;
    @Column(name = "field_32_6459")
    private LocalDate field326459;
    @Column(name = "field_33_9260")
    private BigDecimal field339260;
    @Column(name = "field_34_7976")
    private Long field347976;
    @Column(name = "field_35_3205")
    private String field353205;
    @Column(name = "field_36_9090")
    private BigDecimal field369090;
    @Column(name = "field_37_3971")
    private UUID field373971;
    @Column(name = "field_38_6633")
    private String field386633;
    @Column(name = "field_39_3731")
    private BigDecimal field393731;
    @Column(name = "field_40_4858")
    private String field404858;
    @Column(name = "field_41_3484")
    private Integer field413484;
    @Column(name = "field_42_3570")
    private Long field423570;
    @Column(name = "field_43_5088")
    private String field435088;
    @Column(name = "field_44_5988")
    private Boolean field445988;
    @Column(name = "field_45_4169")
    private Long field454169;
    @Column(name = "field_46_4058")
    private String field464058;
    @Column(name = "field_47_2604")
    private Double field472604;
    @Column(name = "field_48_3148")
    private LocalDateTime field483148;
    @Column(name = "field_49_4978")
    private Integer field494978;
    @Column(name = "field_50_8467")
    private UUID field508467;
    @Column(name = "field_51_7476")
    private String field517476;
    @Column(name = "field_52_1897")
    private LocalDateTime field521897;
    @Column(name = "field_53_1349")
    private LocalDate field531349;
    @Column(name = "field_54_9621")
    private UUID field549621;
    @Column(name = "field_55_4562")
    private Long field554562;
    @Column(name = "field_56_9512")
    private String field569512;
    @Column(name = "field_57_2714")
    private Double field572714;
    @Column(name = "field_58_2352")
    private LocalDate field582352;
    @Column(name = "field_59_5555")
    private Long field595555;
    @Column(name = "field_60_7793")
    private Boolean field607793;
    @Column(name = "field_61_9660")
    private Long field619660;
    @Column(name = "field_62_4685")
    private LocalDate field624685;
    @Column(name = "field_63_6749")
    private Boolean field636749;
    @Column(name = "field_64_2890")
    private LocalDateTime field642890;
    @Column(name = "field_65_3695")
    private Long field653695;
    @Column(name = "field_66_5759")
    private String field665759;
    @Column(name = "field_67_9684")
    private Boolean field679684;
    @Column(name = "field_68_3501")
    private Integer field683501;
    @Column(name = "field_69_4031")
    private UUID field694031;
    @Column(name = "field_70_8795")
    private LocalDateTime field708795;
    @Column(name = "field_71_5253")
    private BigDecimal field715253;
    @Column(name = "field_72_8982")
    private BigDecimal field728982;
    @Column(name = "field_73_4410")
    private Long field734410;
    @Column(name = "field_74_6067")
    private Boolean field746067;
    @Column(name = "field_75_9991")
    private BigDecimal field759991;
    @Column(name = "field_76_1881")
    private BigDecimal field761881;
    @Column(name = "field_77_8044")
    private Boolean field778044;
    @Column(name = "field_78_6956")
    private LocalDateTime field786956;
    @Column(name = "field_79_8119")
    private BigDecimal field798119;
    @Column(name = "field_80_5090")
    private Integer field805090;
    @Column(name = "field_81_8766")
    private LocalDateTime field818766;
    @Column(name = "field_82_3589")
    private Long field823589;
    @Column(name = "field_83_3498")
    private Long field833498;
    @Column(name = "field_84_4908")
    private Long field844908;
    @Column(name = "field_85_9198")
    private Double field859198;
    @Column(name = "field_86_7642")
    private Double field867642;
    @Column(name = "field_87_7212")
    private LocalDateTime field877212;
    @Column(name = "field_88_2801")
    private Double field882801;
    @Column(name = "field_89_3285")
    private Boolean field893285;
    @Column(name = "field_90_6145")
    private Boolean field906145;
    @Column(name = "field_91_3809")
    private Boolean field913809;
    @Column(name = "field_92_2356")
    private UUID field922356;
    @Column(name = "field_93_9097")
    private String field939097;
    @Column(name = "field_94_7397")
    private Double field947397;
    @Column(name = "field_95_2622")
    private LocalDate field952622;
    @Column(name = "field_96_4752")
    private BigDecimal field964752;
    @Column(name = "field_97_5306")
    private Double field975306;
    @Column(name = "field_98_1252")
    private LocalDateTime field981252;
    @Column(name = "field_99_2154")
    private UUID field992154;
    @Column(name = "field_100_7887")
    private Double field1007887;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getField19057() {
        return field19057;
    }

    public void setField19057(Double field19057) {
        this.field19057 = field19057;
    }

    public String getField25114() {
        return field25114;
    }

    public void setField25114(String field25114) {
        this.field25114 = field25114;
    }

    public Long getField36452() {
        return field36452;
    }

    public void setField36452(Long field36452) {
        this.field36452 = field36452;
    }

    public LocalDateTime getField42750() {
        return field42750;
    }

    public void setField42750(LocalDateTime field42750) {
        this.field42750 = field42750;
    }

    public LocalDateTime getField59872() {
        return field59872;
    }

    public void setField59872(LocalDateTime field59872) {
        this.field59872 = field59872;
    }

    public BigDecimal getField61938() {
        return field61938;
    }

    public void setField61938(BigDecimal field61938) {
        this.field61938 = field61938;
    }

    public BigDecimal getField79106() {
        return field79106;
    }

    public void setField79106(BigDecimal field79106) {
        this.field79106 = field79106;
    }

    public String getField88753() {
        return field88753;
    }

    public void setField88753(String field88753) {
        this.field88753 = field88753;
    }

    public Integer getField97101() {
        return field97101;
    }

    public void setField97101(Integer field97101) {
        this.field97101 = field97101;
    }

    public UUID getField101384() {
        return field101384;
    }

    public void setField101384(UUID field101384) {
        this.field101384 = field101384;
    }

    public LocalDateTime getField116429() {
        return field116429;
    }

    public void setField116429(LocalDateTime field116429) {
        this.field116429 = field116429;
    }

    public String getField124505() {
        return field124505;
    }

    public void setField124505(String field124505) {
        this.field124505 = field124505;
    }

    public UUID getField139215() {
        return field139215;
    }

    public void setField139215(UUID field139215) {
        this.field139215 = field139215;
    }

    public LocalDateTime getField147719() {
        return field147719;
    }

    public void setField147719(LocalDateTime field147719) {
        this.field147719 = field147719;
    }

    public BigDecimal getField153200() {
        return field153200;
    }

    public void setField153200(BigDecimal field153200) {
        this.field153200 = field153200;
    }

    public Integer getField168060() {
        return field168060;
    }

    public void setField168060(Integer field168060) {
        this.field168060 = field168060;
    }

    public BigDecimal getField178435() {
        return field178435;
    }

    public void setField178435(BigDecimal field178435) {
        this.field178435 = field178435;
    }

    public BigDecimal getField182474() {
        return field182474;
    }

    public void setField182474(BigDecimal field182474) {
        this.field182474 = field182474;
    }

    public UUID getField195205() {
        return field195205;
    }

    public void setField195205(UUID field195205) {
        this.field195205 = field195205;
    }

    public Long getField203216() {
        return field203216;
    }

    public void setField203216(Long field203216) {
        this.field203216 = field203216;
    }

    public Boolean getField215842() {
        return field215842;
    }

    public void setField215842(Boolean field215842) {
        this.field215842 = field215842;
    }

    public BigDecimal getField221701() {
        return field221701;
    }

    public void setField221701(BigDecimal field221701) {
        this.field221701 = field221701;
    }

    public BigDecimal getField236331() {
        return field236331;
    }

    public void setField236331(BigDecimal field236331) {
        this.field236331 = field236331;
    }

    public UUID getField247480() {
        return field247480;
    }

    public void setField247480(UUID field247480) {
        this.field247480 = field247480;
    }

    public UUID getField251425() {
        return field251425;
    }

    public void setField251425(UUID field251425) {
        this.field251425 = field251425;
    }

    public UUID getField269839() {
        return field269839;
    }

    public void setField269839(UUID field269839) {
        this.field269839 = field269839;
    }

    public Boolean getField274318() {
        return field274318;
    }

    public void setField274318(Boolean field274318) {
        this.field274318 = field274318;
    }

    public Boolean getField285891() {
        return field285891;
    }

    public void setField285891(Boolean field285891) {
        this.field285891 = field285891;
    }

    public Double getField294183() {
        return field294183;
    }

    public void setField294183(Double field294183) {
        this.field294183 = field294183;
    }

    public Long getField301657() {
        return field301657;
    }

    public void setField301657(Long field301657) {
        this.field301657 = field301657;
    }

    public UUID getField311701() {
        return field311701;
    }

    public void setField311701(UUID field311701) {
        this.field311701 = field311701;
    }

    public LocalDate getField326459() {
        return field326459;
    }

    public void setField326459(LocalDate field326459) {
        this.field326459 = field326459;
    }

    public BigDecimal getField339260() {
        return field339260;
    }

    public void setField339260(BigDecimal field339260) {
        this.field339260 = field339260;
    }

    public Long getField347976() {
        return field347976;
    }

    public void setField347976(Long field347976) {
        this.field347976 = field347976;
    }

    public String getField353205() {
        return field353205;
    }

    public void setField353205(String field353205) {
        this.field353205 = field353205;
    }

    public BigDecimal getField369090() {
        return field369090;
    }

    public void setField369090(BigDecimal field369090) {
        this.field369090 = field369090;
    }

    public UUID getField373971() {
        return field373971;
    }

    public void setField373971(UUID field373971) {
        this.field373971 = field373971;
    }

    public String getField386633() {
        return field386633;
    }

    public void setField386633(String field386633) {
        this.field386633 = field386633;
    }

    public BigDecimal getField393731() {
        return field393731;
    }

    public void setField393731(BigDecimal field393731) {
        this.field393731 = field393731;
    }

    public String getField404858() {
        return field404858;
    }

    public void setField404858(String field404858) {
        this.field404858 = field404858;
    }

    public Integer getField413484() {
        return field413484;
    }

    public void setField413484(Integer field413484) {
        this.field413484 = field413484;
    }

    public Long getField423570() {
        return field423570;
    }

    public void setField423570(Long field423570) {
        this.field423570 = field423570;
    }

    public String getField435088() {
        return field435088;
    }

    public void setField435088(String field435088) {
        this.field435088 = field435088;
    }

    public Boolean getField445988() {
        return field445988;
    }

    public void setField445988(Boolean field445988) {
        this.field445988 = field445988;
    }

    public Long getField454169() {
        return field454169;
    }

    public void setField454169(Long field454169) {
        this.field454169 = field454169;
    }

    public String getField464058() {
        return field464058;
    }

    public void setField464058(String field464058) {
        this.field464058 = field464058;
    }

    public Double getField472604() {
        return field472604;
    }

    public void setField472604(Double field472604) {
        this.field472604 = field472604;
    }

    public LocalDateTime getField483148() {
        return field483148;
    }

    public void setField483148(LocalDateTime field483148) {
        this.field483148 = field483148;
    }

    public Integer getField494978() {
        return field494978;
    }

    public void setField494978(Integer field494978) {
        this.field494978 = field494978;
    }

    public UUID getField508467() {
        return field508467;
    }

    public void setField508467(UUID field508467) {
        this.field508467 = field508467;
    }

    public String getField517476() {
        return field517476;
    }

    public void setField517476(String field517476) {
        this.field517476 = field517476;
    }

    public LocalDateTime getField521897() {
        return field521897;
    }

    public void setField521897(LocalDateTime field521897) {
        this.field521897 = field521897;
    }

    public LocalDate getField531349() {
        return field531349;
    }

    public void setField531349(LocalDate field531349) {
        this.field531349 = field531349;
    }

    public UUID getField549621() {
        return field549621;
    }

    public void setField549621(UUID field549621) {
        this.field549621 = field549621;
    }

    public Long getField554562() {
        return field554562;
    }

    public void setField554562(Long field554562) {
        this.field554562 = field554562;
    }

    public String getField569512() {
        return field569512;
    }

    public void setField569512(String field569512) {
        this.field569512 = field569512;
    }

    public Double getField572714() {
        return field572714;
    }

    public void setField572714(Double field572714) {
        this.field572714 = field572714;
    }

    public LocalDate getField582352() {
        return field582352;
    }

    public void setField582352(LocalDate field582352) {
        this.field582352 = field582352;
    }

    public Long getField595555() {
        return field595555;
    }

    public void setField595555(Long field595555) {
        this.field595555 = field595555;
    }

    public Boolean getField607793() {
        return field607793;
    }

    public void setField607793(Boolean field607793) {
        this.field607793 = field607793;
    }

    public Long getField619660() {
        return field619660;
    }

    public void setField619660(Long field619660) {
        this.field619660 = field619660;
    }

    public LocalDate getField624685() {
        return field624685;
    }

    public void setField624685(LocalDate field624685) {
        this.field624685 = field624685;
    }

    public Boolean getField636749() {
        return field636749;
    }

    public void setField636749(Boolean field636749) {
        this.field636749 = field636749;
    }

    public LocalDateTime getField642890() {
        return field642890;
    }

    public void setField642890(LocalDateTime field642890) {
        this.field642890 = field642890;
    }

    public Long getField653695() {
        return field653695;
    }

    public void setField653695(Long field653695) {
        this.field653695 = field653695;
    }

    public String getField665759() {
        return field665759;
    }

    public void setField665759(String field665759) {
        this.field665759 = field665759;
    }

    public Boolean getField679684() {
        return field679684;
    }

    public void setField679684(Boolean field679684) {
        this.field679684 = field679684;
    }

    public Integer getField683501() {
        return field683501;
    }

    public void setField683501(Integer field683501) {
        this.field683501 = field683501;
    }

    public UUID getField694031() {
        return field694031;
    }

    public void setField694031(UUID field694031) {
        this.field694031 = field694031;
    }

    public LocalDateTime getField708795() {
        return field708795;
    }

    public void setField708795(LocalDateTime field708795) {
        this.field708795 = field708795;
    }

    public BigDecimal getField715253() {
        return field715253;
    }

    public void setField715253(BigDecimal field715253) {
        this.field715253 = field715253;
    }

    public BigDecimal getField728982() {
        return field728982;
    }

    public void setField728982(BigDecimal field728982) {
        this.field728982 = field728982;
    }

    public Long getField734410() {
        return field734410;
    }

    public void setField734410(Long field734410) {
        this.field734410 = field734410;
    }

    public Boolean getField746067() {
        return field746067;
    }

    public void setField746067(Boolean field746067) {
        this.field746067 = field746067;
    }

    public BigDecimal getField759991() {
        return field759991;
    }

    public void setField759991(BigDecimal field759991) {
        this.field759991 = field759991;
    }

    public BigDecimal getField761881() {
        return field761881;
    }

    public void setField761881(BigDecimal field761881) {
        this.field761881 = field761881;
    }

    public Boolean getField778044() {
        return field778044;
    }

    public void setField778044(Boolean field778044) {
        this.field778044 = field778044;
    }

    public LocalDateTime getField786956() {
        return field786956;
    }

    public void setField786956(LocalDateTime field786956) {
        this.field786956 = field786956;
    }

    public BigDecimal getField798119() {
        return field798119;
    }

    public void setField798119(BigDecimal field798119) {
        this.field798119 = field798119;
    }

    public Integer getField805090() {
        return field805090;
    }

    public void setField805090(Integer field805090) {
        this.field805090 = field805090;
    }

    public LocalDateTime getField818766() {
        return field818766;
    }

    public void setField818766(LocalDateTime field818766) {
        this.field818766 = field818766;
    }

    public Long getField823589() {
        return field823589;
    }

    public void setField823589(Long field823589) {
        this.field823589 = field823589;
    }

    public Long getField833498() {
        return field833498;
    }

    public void setField833498(Long field833498) {
        this.field833498 = field833498;
    }

    public Long getField844908() {
        return field844908;
    }

    public void setField844908(Long field844908) {
        this.field844908 = field844908;
    }

    public Double getField859198() {
        return field859198;
    }

    public void setField859198(Double field859198) {
        this.field859198 = field859198;
    }

    public Double getField867642() {
        return field867642;
    }

    public void setField867642(Double field867642) {
        this.field867642 = field867642;
    }

    public LocalDateTime getField877212() {
        return field877212;
    }

    public void setField877212(LocalDateTime field877212) {
        this.field877212 = field877212;
    }

    public Double getField882801() {
        return field882801;
    }

    public void setField882801(Double field882801) {
        this.field882801 = field882801;
    }

    public Boolean getField893285() {
        return field893285;
    }

    public void setField893285(Boolean field893285) {
        this.field893285 = field893285;
    }

    public Boolean getField906145() {
        return field906145;
    }

    public void setField906145(Boolean field906145) {
        this.field906145 = field906145;
    }

    public Boolean getField913809() {
        return field913809;
    }

    public void setField913809(Boolean field913809) {
        this.field913809 = field913809;
    }

    public UUID getField922356() {
        return field922356;
    }

    public void setField922356(UUID field922356) {
        this.field922356 = field922356;
    }

    public String getField939097() {
        return field939097;
    }

    public void setField939097(String field939097) {
        this.field939097 = field939097;
    }

    public Double getField947397() {
        return field947397;
    }

    public void setField947397(Double field947397) {
        this.field947397 = field947397;
    }

    public LocalDate getField952622() {
        return field952622;
    }

    public void setField952622(LocalDate field952622) {
        this.field952622 = field952622;
    }

    public BigDecimal getField964752() {
        return field964752;
    }

    public void setField964752(BigDecimal field964752) {
        this.field964752 = field964752;
    }

    public Double getField975306() {
        return field975306;
    }

    public void setField975306(Double field975306) {
        this.field975306 = field975306;
    }

    public LocalDateTime getField981252() {
        return field981252;
    }

    public void setField981252(LocalDateTime field981252) {
        this.field981252 = field981252;
    }

    public UUID getField992154() {
        return field992154;
    }

    public void setField992154(UUID field992154) {
        this.field992154 = field992154;
    }

    public Double getField1007887() {
        return field1007887;
    }

    public void setField1007887(Double field1007887) {
        this.field1007887 = field1007887;
    }

}