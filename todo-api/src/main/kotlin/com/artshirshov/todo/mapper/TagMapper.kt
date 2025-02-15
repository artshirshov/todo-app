package com.artshirshov.todo.mapper

import com.artshirshov.todo.domain.Tag
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface TagMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    fun toEntity(name: String): Tag
}