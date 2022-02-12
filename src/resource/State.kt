package resource

enum class State {

    PARSE_SUCCESS,//解析成功
    PARSE_NOT_EXPECT_VALUE,//非预期值
    PARSE_INVALID_VALUE,//非法字符
    PARSE_ROOT_NOT_SINGULAR//多个根错误

}