package cucumber.rediss.domain;

public enum Category {
    num0("디지털기기"),num1("생활가전"),num2("가구/인테리어"),num3("유아용품"),num4("뷰티/미용"),num5("의류"),
    num6("스포츠/레져"),num7("도서"),num8("반려동물"), num9("식물"),num10("기타"), //물건
    num11("서울"),num12("경기/인천"),num13("강원도"),num14("충청북도"),num15("충청남도"),
    num16("전라북도"),num17("전라남도"),num18("경상북도"),num19("경상남도"), num20("제주도");    //지역

    private String tr_num; //translate_number

    Category(String tr_num){
        this.tr_num=tr_num;
    }

    public String getTr_num(){
        return tr_num;
    }
}
