#include "tree/Tree.hpp"

void Splay::rotate_left(Splay::Node* node) const noexcept {
    Node* father = node->parent;
    Node* a      = node->right;
    Node* b      = a->left;

    if (father) {
        if (father->left == node) {
            father->left = a;
        } else {
            father->right = a;
        }
    }

    if (b) {
        b->parent = node;
    }

    a->parent    = father;
    a->left      = node;
    node->parent = a;
    node->right  = b;
}

void Splay::rotate_right(Splay::Node* node) const noexcept {
    Node* father = node->parent;
    Node* a      = node->left;
    Node* b      = a->right;

    if (father) {
        if (father->left == node) {
            father->left = a;
        } else {
            father->right = a;
        }
    }

    if (b) {
        b->parent = node;
    }

    a->parent    = father;
    a->right     = node;
    node->parent = a;
    node->left   = b;
}

bool Splay::zig(Splay::Node* x) const noexcept {
    Node* father = x->parent;
    if (father->left == x) {
        rotate_right(father);
    } else {
        rotate_left(father);
    }
    return true;
}

bool Splay::zig_zag(Splay::Node* x) const noexcept {
    Node* father       = x->parent;
    Node* grand_father = father->parent;
    bool change        = false;
    if (grand_father->left == father && father->right == x) {
        rotate_left(father);
        rotate_right(grand_father);
        change = true;
    } else if (grand_father->right == father && father->left == x) {
        rotate_right(father);
        rotate_left(grand_father);
        change = true;
    }
    return change;
}

bool Splay::zig_zig(Splay::Node* x) const noexcept {
    Node* father       = x->parent;
    Node* grand_father = father->parent;
    bool change        = false;
    if (grand_father->left == father && father->left == x) {
        rotate_right(grand_father);
        rotate_right(father);
        change = true;
    } else if (grand_father->right == father && father->right == x) {
        rotate_left(grand_father);
        rotate_left(father);
        change = true;
    }
    return change;
}

void Splay::splay(Splay::Node* x) const noexcept {
    while (x->parent) {
        Node* father       = x->parent;
        Node* grand_father = father->parent;

        if (!grand_father) {
            zig(x);
        } else {
            bool success = zig_zag(x);
            if (!success) {
                zig_zig(x);
            }
        }
    }
    root = x;
}

Splay::Node* Splay::find(int value) const noexcept {
    Node* current_node = root;
    while (true) {
        if (!current_node) {
            break;
        }
        if (current_node->value == value) {
            break;
        } else if (current_node->value < value) {
            current_node = current_node->right;
        } else {
            current_node = current_node->left;
        }
    }
    if (current_node) {
        splay(current_node);
    }
    return current_node;
}

bool Splay::contains(int value) const noexcept {
    return find(value);
}

Splay::Node* Splay::insert(Splay::Node*& node, Splay::Node*& prev_node, int value) {
    if (!node) {
        node = new Node(value, nullptr, nullptr, prev_node);
        return node;
    }
    if (node->value < value) {
        return insert(node->right, node, value);
    } else if (node->value > value) {
        return insert(node->left, node, value);
    } else {
        return nullptr;
    }
}

bool Splay::insert(int value) {
    Splay::Node* abstract_node = nullptr;
    Node* result               = insert(root, abstract_node, value);
    if (result) {
        tree_size++;
        splay(result);
    }
    return result;
}

bool Splay::remove(int value) {
    if (!find(value)) {
        return false;
    }
    tree_size--;
    Node* current_node = root;
    while (true) {
        if (current_node->value == value) {
            break;
        } else if (current_node->value < value) {
            current_node = current_node->right;
        } else {
            current_node = current_node->left;
        }
    }
    splay(current_node);

    Node* l = root->left;
    Node* r = root->right;

    root->left  = nullptr;
    root->right = nullptr;
    delete root;
    root = nullptr;

    if (!r && l) {
        root         = l;
        root->parent = nullptr;
    } else if (r && !l) {
        root         = r;
        root->parent = nullptr;
    } else if (r && l) {
        root           = l;
        root->parent   = nullptr;
        Node* max_node = l;
        while (true) {
            if (max_node->right && max_node->right->value > max_node->value) {
                max_node = max_node->right;
            } else {
                break;
            }
        }
        splay(max_node);
        root->right = r;
        r->parent   = root;
    }
    return true;
}

std::size_t Splay::size() const noexcept {
    return tree_size;
}

bool Splay::empty() const noexcept {
    return tree_size == 0;
}

void Splay::dump_tree(Node* node, std::vector<int>& tree) const noexcept {
    if (node->left) {
        dump_tree(node->left, tree);
    }
    tree.push_back(node->value);
    if (node->right) {
        dump_tree(node->right, tree);
    }
}

std::vector<int> Splay::values() const noexcept {
    std::vector<int> tree;
    tree.reserve(tree_size);
    dump_tree(root, tree);
    return tree;
}

Splay::Node::~Node() {
    delete left;
    delete right;
}

Splay::~Splay() {
    delete root;
}