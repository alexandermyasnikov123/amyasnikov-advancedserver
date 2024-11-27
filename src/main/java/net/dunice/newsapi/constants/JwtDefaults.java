package net.dunice.newsapi.constants;

public interface JwtDefaults {
    String HEADER_PREFIX = "Bearer ";

    String USERNAME_CLAIM = "username";

    String ROLE_CLAIM = "role";

    String ID_CLAIM = "uuid";

    String BEARER_SECRET_HS512 =
            """
            ad4128cc7416e9bc72e3fe998f8f9a13d984fc9d5f9bedea916e3b028e16bbcbd\
            8795120805a945302def69ee6429a26d4787d5b6ab7d6d916a6e3832e33be232e\
            8a6ab91569d9e59d9b30638ba7adeccb583f722304dc48959e23634cac5dfeb19\
            d91f74f55c7124806c63346daadfcba09cd4054c23eeac4e5cbd2baf15dc412a7\
            0d81455d6c21549f4e23b41510d0148b58f1ecb0461c7769793c4f540adf8b405\
            760417bd14f27028dab6bbab517836c95d3afa31f2bc1ddf2b074b7d58182a9d5\
            908cdfd338632f5baa85e6da403ad7dd415609714dfd6e37a50761b71ff382364\
            1430c8791f7b7f40e9e6a8e1d5cd797b95953f0a5abf819a278e67589\
            """;

    int EXPIRATION_MINUTES = 10;
}
