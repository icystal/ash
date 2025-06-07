package fun.icystal.act.mapper;

import fun.icystal.entity.SubscribeInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubscribeInfoMapper {

    @Insert({
            "INSERT INTO subscribe_info (email, type) VALUES (#{info.email}, #{info.type})"
    })
    Integer insert(@Param("info") SubscribeInfo info);

    @Select({
            "SELECT * FROM subscribe_info WHERE subscribe_type = #{type}"
    })
    List<SubscribeInfo> queryByType(@Param("type") Integer type);
}
