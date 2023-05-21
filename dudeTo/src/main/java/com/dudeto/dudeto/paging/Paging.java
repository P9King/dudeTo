package com.dudeto.dudeto.paging;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Paging {
    public int showListCount = 0; // 글 개수
    public int pageSize = 0;  // 페이지 개수
    public Long totalBoardCount = 0L; // 총 글 개수
    public int curPage = 0; // 현재 페이지로 쓸 변수

    public Paging() {
    }

    public Paging(int showListCount, int pageSize, Long writing_Count, int curPage) {
        super();
        this.showListCount = showListCount;
        this.pageSize = pageSize;
        this.totalBoardCount = writing_Count;
        this.curPage = curPage;
    }

    //총 필요한 페이지 개수
    public Long getTotalPage_Count()
    {
        return ( (totalBoardCount - 1) / showListCount) + 1;
    }

    //페이지 시작 숫자
    public int getPage_Start()
    {
        return ( ( curPage - 1 ) / pageSize) * pageSize + 1;
    }

    //페이지 마지막 숫자
    public Long getPage_End()
    {
        return Math.min( getPage_Start() + pageSize - 1 , getTotalPage_Count() );
    }

    //Pre 표시 여부
    public boolean isPre()
    {
        return getPage_Start() != 1;
    }

    //Next 표시 여부
    public boolean isNext()
    {
        return getPage_End() < getTotalPage_Count();
    }

    //글 범위 시작 번호
    public int getWriting_Start()
    {
        return getWriting_End() - showListCount + 1;
    }

    //글 범위 끝 번호
    public int getWriting_End()
    {
        return curPage * showListCount;
    }

}
