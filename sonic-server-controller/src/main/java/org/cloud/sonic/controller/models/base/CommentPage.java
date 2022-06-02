package org.cloud.sonic.controller.models.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用分页对象
 *
 * @author JayWenStar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentPage<T> implements Serializable {

    /**
     * 页大小
     */
    private long size;

    /**
     * 页内容
     */
    private List<T> content;

    /**
     * 当前页码
     */
    private long number;

    /**
     * 页内容总个数
     */
    private long totalElements;

    /**
     * 总页数
     */
    private long totalPages;

    public static <T> CommentPage<T> convertFrom(Page<T> page) {
        return new CommentPage<>(
                page.getSize(), page.getRecords(), page.getCurrent() - 1, page.getTotal(), page.getPages()
        );
    }

    /**
     * Page的数据会被content替代
     */
    public static <T> CommentPage<T> convertFrom(Page<?> page, List<T> content) {
        return new CommentPage<>(
                page.getSize(), content, page.getCurrent() - 1, page.getTotal(), page.getPages()
        );
    }


    public static <T> CommentPage<T> emptyPage() {
        return new CommentPage<>(0, new ArrayList<>(), 0, 0, 0);
    }
}
