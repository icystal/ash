package fun.icystal.act.mapper;

import fun.icystal.entity.CaiLianHeadline;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CaiLianHeadLineMapper {

    @Insert({
            "<script>",
            "INSERT INTO cai_lian_headline (title, link, brief, publication_time) VALUES ",
            "<foreach collection='list' item='item' separator=','>",
                "(#{item.title}, #{item.link}, #{item.brief}, #{item.publicationTime})",
            "</foreach>",
            "</script>"
    })
    Integer insert(List<CaiLianHeadline> items);

    @Select({
            "SELECT * FROM cai_lian_headline",
            "WHERE publication_time >= #{start} AND publication_time <= #{end}",
            "ORDER BY publication_time DESC",
    })
    List<CaiLianHeadline> selectByDuration(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
