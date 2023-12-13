// eslint-disable-next-line react/prop-types
function ProductCard ({ product }) {
    // eslint-disable-next-line react/prop-types
    const { imageUrl, title, description, category, itemId } = product;
    function formatItemType(type) {
        switch (type) {
            case 'secondHandItem':
                return 'Second Hand Item';
            case 'donatedItem':
                return 'Donated Item';
            case 'lostItem':
                return 'Lost Item';
            case 'foundItem':
                return 'Found Item';
            case 'lendItem':
                return 'Lend Item';
            case 'rentedItem':
                return 'Rented Item';
            default:
                return 'Unknown Type';
        }
    }

    function getBorderColorClass(type) {
        switch (type) {
            case 'secondHandItem':
                return 'border-blue-500';
            case 'donatedItem':
                return 'border-green-500';
            case 'lostItem':
                return 'border-red-500';
            case 'foundItem':
                return 'border-yellow-500';
            case 'lendItem':
                return 'border-purple-500';
            case 'rentedItem':
                return 'border-orange-500';
            default:
                return 'border-gray-300';
        }
    }

    function getTextColorClass(type) {
        switch (type) {
            case 'secondHandItem':
                return 'hover:text-blue-500';
            case 'donatedItem':
                return 'hover:text-green-500';
            case 'lostItem':
                return 'hover:text-red-500';
            case 'foundItem':
                return 'hover:text-yellow-500';
            case 'lendItem':
                return 'hover:text-purple-500';
            case 'rentedItem':
                return 'hover:text-orange-500';
            default:
                return 'hover:text-gray-300';
        }
    }

    const itemTypeFormatted = formatItemType(category);

    return(
        <div className={`border border-3 ${getTextColorClass(category)} rounded bg-white ${getBorderColorClass(category)}`}>
            <img className="p-[16px]" src={imageUrl} alt="item-photo" />
            <div className={`p-[16px] text-center space-y-[8px] border-b ${getBorderColorClass(category)}`}>
                <a href="" className="productCardTitle">{title}</a>
                <h6 className="productCardExplanation truncate">{description}</h6>
            </div>
            <div className="p-[16px] text-center">
                <h6 className={`productCardCategory mb-[16px] pb-[16px] border-b ${getBorderColorClass(category)}`}>{itemTypeFormatted}</h6>
                <a href={`/view-details/${itemId}`} className="hover:underline productCardTitle hover:text-blue-text">
                    <div className="flex items-center text-sm justify-center">
                        View Details
                        <svg className="rtl:rotate-180 w-3.5 h-3.5 ms-2" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 10">
                            <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M1 5h12m0 0L9 1m4 4L9 9"/>
                        </svg>
                    </div>
                </a>
            </div>
        </div>
    );
}
export default ProductCard;