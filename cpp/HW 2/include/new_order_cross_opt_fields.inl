#if !defined(FIELD) || !defined(CONTRA_FIELD)
#error You need to define FIELD and CONTRA_FIELD macro
#else
FIELD(symbol, 1, 1)
CONTRA_FIELD(algoritmic_indicator, 1, 64)
#undef FIELD
#undef CONTRA_FIELD
#endif
