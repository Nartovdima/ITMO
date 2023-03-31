#include "requests.h"

namespace {

void encode_new_order_opt_fields(unsigned char * bitfield_start,
                                 const double price,
                                 const char ord_type,
                                 const char time_in_force,
                                 const unsigned max_floor,
                                 const std::string & symbol,
                                 const char capacity,
                                 const std::string & account)
{
    auto * p = bitfield_start + new_order_bitfield_num();
#define FIELD(name, bitfield_num, bit)                    \
    set_opt_field_bit(bitfield_start, bitfield_num, bit); \
    p = encode_field_##name(p, name);
#include "new_order_opt_fields.inl"
}

unsigned char * add_new_order_cross_opt_fields(unsigned char * bitfield_start)
{
    auto * p = bitfield_start + new_order_cross_bitfield_num();

#define FIELD(name, bitfield_num, bit) \
    set_opt_field_bit(bitfield_start, bitfield_num, bit);

#define CONTRA_FIELD(name, bitfield_num, bit) \
    set_opt_field_bit(bitfield_start, bitfield_num, bit);

#include "new_order_cross_opt_fields.inl"
    return p;
}

unsigned char * add_new_order_cross_multileg_opt_fields(unsigned char * bitfield_start)
{
    auto * p = bitfield_start + new_order_cross_multileg_bitfield_num();
#define FIELD(name, bitfield_num, bit) \
    set_opt_field_bit(bitfield_start, bitfield_num, bit);

#define CONTRA_FIELD(name, bitfield_num, bit) \
    set_opt_field_bit(bitfield_start, bitfield_num, bit);

#include "new_order_cross_multileg_opt_fields.inl"
    return p;
}

unsigned char * encode_new_order_cross_opt_fields(unsigned char * bitfield_start,
                                                  const std::string & symbol)
{
    auto * p = bitfield_start;
#define FIELD(name, bitfield_num, bit) \
    p = encode_field_##name(p, name);
#define CONTRA_FIELD(_, __, ___)
#include "new_order_cross_opt_fields.inl"
    return p;
}

unsigned char * encode_new_order_cross_multileg_opt_fields(unsigned char * bitfield_start,
                                                           const std::string & symbol)
{
    auto * p = bitfield_start;
#define FIELD(name, bitfield_num, bit) \
    p = encode_field_##name(p, name);
#define CONTRA_FIELD(_, __, ___)
#include "new_order_cross_multileg_opt_fields.inl"
    return p;
}

unsigned char * encode_order_opt_fields(unsigned char * start,
                                        const std::string & algoritmic_indicator)
{
    auto * p = start;
#define FIELD(name) \
    p = encode_field_##name(p, name);
#include "order_opt_fields.inl"
    return p;
}

unsigned char * encode_complex_order_opt_fields(unsigned char * start,
                                                const std::string & leg_position_effects,
                                                const std::string & algoritmic_indicator)
{
    auto * p = start;
#define FIELD(name) \
    p = encode_field_##name(p, name);
#include "complex_order_opt_fields.inl"
    return p;
}

uint8_t encode_request_type(const RequestType type)
{
    switch (type) {
    case RequestType::New:
        return 0x38;
    case RequestType::NewCross:
        return 0x7A;
    case RequestType::NewCrossMultileg:
        return 0x85;
    }
    return 0;
}

unsigned char * add_request_header(unsigned char * start,
                                   unsigned length,
                                   const RequestType type,
                                   unsigned seq_no)
{
    *start++ = 0xBA;
    *start++ = 0xBA;
    start = encode(start, static_cast<uint16_t>(length));
    start = encode(start, encode_request_type(type));
    *start++ = 0;
    return encode(start, static_cast<uint32_t>(seq_no));
}

char convert_side(const Side side)
{
    switch (side) {
    case Side::Buy: return '1';
    case Side::Sell: return '2';
    }
    return 0;
}

char convert_ord_type(const OrdType ord_type)
{
    switch (ord_type) {
    case OrdType::Market: return '1';
    case OrdType::Limit: return '2';
    case OrdType::Pegged: return 'P';
    }
    return 0;
}

char convert_time_in_force(const TimeInForce time_in_force)
{
    switch (time_in_force) {
    case TimeInForce::Day: return '0';
    case TimeInForce::IOC: return '3';
    case TimeInForce::GTD: return '6';
    }
    return 0;
}

char convert_capacity(const Capacity capacity)
{
    switch (capacity) {
    case Capacity::Agency: return 'A';
    case Capacity::Principal: return 'P';
    case Capacity::RisklessPrincipal: return 'R';
    }
    return 0;
}

char convert_account_type(const AccountType account_type)
{
    switch (account_type) {
    case AccountType::Client: return '1';
    case AccountType::House: return '3';
    }
    return 0;
}

char convert_position(const Position position)
{
    switch (position) {
    case Position::Open: return 'O';
    case Position::Close: return 'C';
    case Position::None: return 'N';
    }
    return 0;
}

std::string convert_legs(const std::vector<Position> & legs)
{
    std::string str;
    str.reserve(legs.size());
    for (const auto p : legs) {
        str += convert_position(p);
    }
    return str;
}

std::string convert_algoritmic_indicator(const bool algoritmic_indicator)
{
    std::string str;
    switch (static_cast<int>(algoritmic_indicator)) {
    case 0: return str.append("N");
    case 1: return str.append("Y");
    }
    return str;
}

unsigned char * add_request_main_part(unsigned char * start,
                                      RequestType request,
                                      unsigned seq_no,
                                      const std::string & cross_id,
                                      double price,
                                      const Order & agency_order,
                                      unsigned message_length)
{
    auto * p = add_request_header(start, message_length - 2, request, seq_no);
    p = encode_text(p, cross_id, 20);
    p = encode_char(p, '1');
    p = encode_char(p, convert_side(agency_order.side));
    p = encode_price(p, price);
    return encode_binary4(p, static_cast<uint32_t>(agency_order.volume));
}

unsigned char * encode_order_main_part(unsigned char * start, const Order & order)
{
    auto * p = encode_char(start, convert_side(order.side));
    p = encode_binary4(p, static_cast<uint32_t>(order.volume));
    p = encode_text(p, order.cl_ord_id, 20); // UB
    p = encode_char(p, convert_capacity(order.capacity));
    p = encode_alpha(p, order.clearing_firm, 4);
    return encode_char(p, convert_account_type(order.account_type));
}

unsigned char * encode_order(unsigned char * start, const Order & order)
{
    auto * p = encode_order_main_part(start, order);
    return encode_order_opt_fields(p, convert_algoritmic_indicator(order.algorithmic_indicator));
}

unsigned char * encode_complex_order(unsigned char * start, const ComplexOrder & order)
{
    auto * p = encode_order_main_part(start, order.order);
    return encode_complex_order_opt_fields(p, convert_legs(order.legs), convert_algoritmic_indicator(order.order.algorithmic_indicator));
}

} // anonymous namespace

std::array<unsigned char, calculate_size(RequestType::New)> create_new_order_request(const unsigned seq_no,
                                                                                     const std::string & cl_ord_id,
                                                                                     const Side side,
                                                                                     const double volume,
                                                                                     const double price,
                                                                                     const OrdType ord_type,
                                                                                     const TimeInForce time_in_force,
                                                                                     const double max_floor,
                                                                                     const std::string & symbol,
                                                                                     const Capacity capacity,
                                                                                     const std::string & account)
{
    static_assert(calculate_size(RequestType::New) == 78, "Wrong New Order message size");

    std::array<unsigned char, calculate_size(RequestType::New)> msg;
    auto * p = add_request_header(&msg[0], msg.size() - 2, RequestType::New, seq_no);
    p = encode_text(p, cl_ord_id, 20);
    p = encode_char(p, convert_side(side));
    p = encode_binary4(p, static_cast<uint32_t>(volume));
    p = encode(p, static_cast<uint8_t>(new_order_bitfield_num()));
    encode_new_order_opt_fields(p,
                                price,
                                convert_ord_type(ord_type),
                                convert_time_in_force(time_in_force),
                                max_floor,
                                symbol,
                                convert_capacity(capacity),
                                account);
    return msg;
}

std::vector<unsigned char> create_new_order_cross_request(
        unsigned seq_no,
        const std::string & cross_id,
        double price,
        const std::string & symbol,
        const Order & agency_order,
        const std::vector<Order> & contra_orders)
{
    std::vector<unsigned char> msg;
    msg.resize(calculate_size(RequestType::NewCross) + order_size() * (contra_orders.size() + 1));
    auto * p = add_request_main_part(&msg[0], RequestType::NewCross, seq_no, cross_id, price, agency_order, static_cast<unsigned int>(msg.size()));
    p = encode(p, static_cast<uint8_t>(new_order_cross_bitfield_num()));
    p = add_new_order_cross_opt_fields(p);

    p = encode_binary2(p, static_cast<uint16_t>(contra_orders.size() + 1));
    p = encode_order(p, agency_order);
    for (const Order & contra_order : contra_orders) {
        p = encode_order(p, contra_order);
    }
    encode_new_order_cross_opt_fields(p, symbol);
    return msg;
}

std::vector<unsigned char> create_new_order_cross_multileg_request(
        unsigned seq_no,
        const std::string & cross_id,
        double price,
        const std::string & symbol,
        const ComplexOrder & agency_order,
        const std::vector<ComplexOrder> & contra_orders)
{
    std::vector<unsigned char> msg;
    msg.resize(calculate_size(RequestType::NewCrossMultileg) + complex_order_size() * (contra_orders.size() + 1));
    auto * p = add_request_main_part(&msg[0], RequestType::NewCrossMultileg, seq_no, cross_id, price, agency_order.order, static_cast<unsigned int>(msg.size()));
    p = encode(p, static_cast<uint8_t>(new_order_bitfield_num()));
    p = add_new_order_cross_multileg_opt_fields(p);

    p = encode_binary2(p, static_cast<uint16_t>(contra_orders.size() + 1));
    p = encode_complex_order(p, agency_order);
    for (const ComplexOrder & contra_order : contra_orders) {
        p = encode_complex_order(p, contra_order);
    }
    encode_new_order_cross_multileg_opt_fields(p, symbol);
    return msg;
}
