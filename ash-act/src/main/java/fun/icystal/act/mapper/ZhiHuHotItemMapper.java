package fun.icystal.act.mapper;

import fun.icystal.ZhiHuHotItem;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ZhiHuHotItemMapper {

    @Insert({
            "<script>",
            "INSERT INTO zhi_hu_hot_item (title, link, excerpt, img_url, sort, fetch_time) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
                "(#{item.title}, #{item.link}, #{item.excerpt}, #{item.imgUrl}, #{item.sort}, #{item.fetchTime})",
            "</foreach>",
            "</script>"
    })
    Integer insert(List<ZhiHuHotItem> items);

    @Select({
            "SELECT * FROM zhi_hu_hot_item",
            "WHERE fetch_time >= #{start} AND fetch_time <= #{end}",
            "ORDER BY fetch_time DESC",
    })
    @Results({
        @Result(property= "imgUrl", column = "img_url"),
        @Result(property = "fetchTime", column = "fetch_time")
    })
    List<ZhiHuHotItem> queryByDuration(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
